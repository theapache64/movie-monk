package com.theapache64.moviemonk.core.providers

import com.theapache64.moviemonk.core.interfaces.MovieProvider
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.utils.DVDPlayerParser
import com.theapache64.moviemonk.utils.RestClient
import com.theapache64.moviemonk.utils.StringUtils
import java.net.URLEncoder

object DVDPlay : MovieProvider {

    const val BASE_URL = "https://dvdplay.run"


    override fun getIssueId(): Int? = 2

    override fun getTrendingMovies(baseDomain: String): List<BaseMovie> {
        val htmlResponse = StringUtils.removeNewLinesAndMultipleSpaces(RestClient.get(BASE_URL, null).body!!.string())
        return DVDPlayerParser.getMovies(htmlResponse)
    }

    override fun getBaseDomain(): String {
        return BASE_URL
    }

    override fun parseMovie(baseDomain: String, newBaseMovie: BaseMovie): Movie {

        val url = "$baseDomain${newBaseMovie.subPath}"
        val innerPageData = StringUtils.removeNewLinesAndMultipleSpaces(
            RestClient.get(
                url,
                null
            ).body!!.string()
        )

        val page1Data = DVDPlayerParser.getPage1Data(newBaseMovie.movie!!.title!!, innerPageData)
        val downloadLinks = DVDPlayerParser.parseFinalDownloadLinks(page1Data.innerPageUrls)

        val baseName = newBaseMovie.movie.baseName
        val infoUrl = "http://google.com/search?q=${URLEncoder.encode(baseName, "UTF-8")}"
        return Movie(
            baseName,
            newBaseMovie.subPath,
            url,
            null,
            infoUrl,
            newBaseMovie.movie.title,
            null,
            1,
            null,
            newBaseMovie.movie.languages,
            null,
            null,
            null,
            null,
            newBaseMovie.movie.year,
            null,
            null,
            null,
            page1Data.posterUrl,
            null,
            null,
            newBaseMovie.movie.quality,
            downloadLinks,
            page1Data.screensUrls
        )

    }


}