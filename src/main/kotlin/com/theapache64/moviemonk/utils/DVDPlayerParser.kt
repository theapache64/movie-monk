package com.theapache64.moviemonk.utils

import com.theapache64.moviemonk.core.providers.DVDPlay
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.models.external.dvdplay.InnerPageUrl
import com.theapache64.moviemonk.models.external.dvdplay.Page1Data
import java.io.File
import java.net.SocketTimeoutException

object DVDPlayerParser {

    private const val TELEGRAM_CHANNEL_SERVER_1_ID = 1
    private const val TELEGRAM_CHANNEL_SERVER_2_ID = 5
    private const val SHARER_SERVER_ID = 2
    private const val DROPBOX_SERVER_ID = 3
    private const val UP_INDIA_SERVER_ID = 4

    private val MOVIE_REGEX =
        "<b> &raquo; (?<movieName>.+?) \\((?<year>\\d+)\\) <span style=\"color:#808080;\"> (?<language>.+?) (?<quality>.+?) <\\/spa><a href=\"(?<url>.+?)\">Click here<\\/a><\\/b>".toRegex()

    private val page1RegEx =
        "<a class=\"touch\" href=\"(?<pageUrl>.+?)\"><b>&raquo; (?<title>.+?)<\\/a>".toRegex()


    private val upIndiaRegEx1 =
        "<a class=\"download_box_new\" href=\"#\" onClick=\"dwl\\(this\\);\" itemlink=\"(?<url>.+?)\">".toRegex()


    private val upIndiaRegEx2 =
        "<a onClick=\"lnk\\(this, 'Download File'\\);\" class=\"mirror_link\" href=\"#\" itemlink=\"(?<url>.+)\"> <b>Download<\\/b> <\\/a>"
            .toRegex()

    private val domainRegEx = "(?:http|https):\\/\\/(?<domain>.+?)\\/".toRegex()


    private val serverRegEx =
        "<p class=\"home\"><span class=\"update\"><a href=\"(?<url>.+?)\">Download File \\(Server (?<serverId>\\d+?)\\)".toRegex()


    private const val SCREEN_URL_REGEX_FORMAT =
        "<div align=\"center\"><img src=\"(?<imageUrl>.+?)\" alt=\"{MOVIE_NAME}\" alt=\"Loading\\.\\.\" style=\"border: #ddd 0px solid;padding: 0px; margin: 0px;\" width=\"100%\"\\/><\\/div>"

    private const val POSTER_URL_REGEX_FORMAT =
        "<img src=\"(?<posterUrl>.+?)\" alt=\"{MOVIE_NAME}"


    fun getPage1Data(movieName: String, htmlResponse: String): Page1Data {
        val posterUrl = parsePosterUrl(movieName, htmlResponse)
        val screenUrls = parseScreenUrls(movieName, htmlResponse)
        val innerPageUrl = getInnerPageUrls(htmlResponse)
        return Page1Data(
            posterUrl,
            screenUrls,
            innerPageUrl
        )
    }

    private fun parseScreenUrls(movieName: String, htmlResponse: String): List<String> {
        val screenUrlRegEx = SCREEN_URL_REGEX_FORMAT.replace(
            "{MOVIE_NAME}",
            movieName
        ).toRegex()
        val urlsList = mutableListOf<String>()
        val matches = screenUrlRegEx.findAll(htmlResponse)
        for (match in matches) {
            urlsList.add(match.groups["imageUrl"]!!.value)
        }
        return urlsList
    }


    private fun parsePosterUrl(movieName: String, htmlResponse: String): String {
        val posterRegEx = POSTER_URL_REGEX_FORMAT.replace(
            "{MOVIE_NAME}", movieName
        ).toRegex()
        return posterRegEx.find(htmlResponse)!!.groups["posterUrl"]!!.value
    }

    private fun getInnerPageUrls(htmlResponse: String): List<InnerPageUrl> {
        val pageUrls = mutableListOf<InnerPageUrl>()
        val matches = page1RegEx.findAll(htmlResponse)
        for (result in matches) {
            val title = result.groups["title"]!!.value
            val pageUrl = result.groups["pageUrl"]!!.value
            pageUrls.add(InnerPageUrl(title, pageUrl))
        }
        return pageUrls
    }

    fun getMovies(htmlResponse: String): List<BaseMovie> {

        val matches = MOVIE_REGEX.findAll(htmlResponse)
        val movieList = mutableListOf<BaseMovie>()

        for (result in matches) {

            val movieName = result.groups["movieName"]!!.value
            val year = result.groups["year"]!!.value.toInt()
            val language = result.groups["language"]!!.value
            val quality = result.groups["quality"]!!.value
            val baseUrl = result.groups["url"]!!.value
            val fullBaseUrl = "${DVDPlay.BASE_URL}$baseUrl"
            val baseName = "$movieName ($year) $language $quality"

            movieList.add(
                BaseMovie(
                    baseName,
                    baseUrl,
                    Movie(
                        baseName = baseName,
                        title = movieName,
                        baseUrl = baseUrl,
                        fullBaseUrl = fullBaseUrl,
                        year = year,
                        languages = language,
                        quality = quality
                    )
                )
            )
        }

        return movieList
    }


    private fun parseDropBoxUrl(url: String): String {
        val respData = StringUtils.removeNewLinesAndMultipleSpaces(RestClient.get(url, null).body!!.string())
        val matches = serverRegEx.find(respData)
        return matches!!.groups["url"]!!.value
    }

    fun parseFinalDownloadLinks(innerPageUrls: List<InnerPageUrl>): List<Movie.Download> {

        val downloadUrls = mutableListOf<Movie.Download>()

        for (iPageUrl in innerPageUrls) {

            val iPageFullUrl = DVDPlay.BASE_URL + iPageUrl.url
            println(iPageFullUrl)
            val pageData = StringUtils.removeNewLinesAndMultipleSpaces(
                RestClient.get(iPageFullUrl, null).body!!.string()
            )

            val matches = serverRegEx.findAll(pageData)

            for (match in matches) {

                val serverId = match.groups["serverId"]!!.value.toInt()

                var url = if (serverId == DROPBOX_SERVER_ID) {
                    "${DVDPlay.BASE_URL}${match.groups["url"]!!.value}"
                } else {
                    match.groups["url"]!!.value
                }

                if (serverId != TELEGRAM_CHANNEL_SERVER_1_ID && serverId != TELEGRAM_CHANNEL_SERVER_2_ID) {


                    try {
                        url = when (serverId) {

                            DROPBOX_SERVER_ID -> {
                                // dropbox
                                parseDropBoxUrl(url)
                            }

                            /*UP_INDIA_SERVER_ID -> {
                                // up india
                                parseUpIndiaUrl(url)
                            }*/

                            else -> {
                                url
                            }
                        }

                    } catch (e: SocketTimeoutException) {
                        e.printStackTrace()
                        println("Failed to some url")
                    }

                    val source = when (serverId) {
                        SHARER_SERVER_ID -> "Sharer"
                        DROPBOX_SERVER_ID -> "DropBox"
                        UP_INDIA_SERVER_ID -> "UpIndia"
                        else -> "DVDPlay"
                    }

                    downloadUrls.add(Movie.Download(source, url.trim(), iPageUrl.title.trim()))
                }
            }

        }

        return downloadUrls
    }


    private fun parseUpIndiaUrl(url: String): String {
        val resp1 = StringUtils.removeNewLinesAndMultipleSpaces(
            RestClient.get(url, null).body!!.string()
        )

        val url1 = "http:" + upIndiaRegEx1.find(resp1)!!.groups["url"]!!.value
        val url2 = url1.replace("?page=mirror", "?page=mirror_country")
        val resp2 = StringUtils.removeNewLinesAndMultipleSpaces(
            RestClient.get(url2, null).body!!.string()
        )
        val url3 = upIndiaRegEx2.find(resp2)!!.groups["url"]!!.value
        val mainDomain = domainRegEx.find(url)!!.groups["domain"]!!.value
        return "http://$mainDomain$url3"
    }


}