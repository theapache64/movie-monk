package com.theapache64.moviemonk.core.providers

import com.theapache64.moviemonk.core.TPBParser
import com.theapache64.moviemonk.core.interfaces.MovieProvider
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.models.external.imdb.IMDBResponse
import com.theapache64.moviemonk.utils.IMDBParser
import com.theapache64.moviemonk.utils.RestClient
import com.theapache64.moviemonk.utils.StringUtils
import java.io.File

object TPB : MovieProvider {

    private const val PROXIES_URL = "https://piratebay-proxylist.net/"
    private val PROXY_REGEX =
        "<span class=\"domain\" style=\"margin-right: 4px;\">(?<domain>.+)</span>".toRegex()

    private val MOVIE_REGEX =
        "<div class=\"detName\"> <a href=\"(?<link>.+?)\" class=\"detLink\" (?:.+?)>(?<movieName>.+?)<\\/a><\\/div>".toRegex()

    override fun getBaseDomain(): String {
        val proxies = getProxies()
        return proxies.first()
    }

    override fun getTrendingMovies(baseDomain: String): List<BaseMovie> {
        println("Collecting trending movies ...")

        println("Proxy : $baseDomain")
        val moviesUrl = "https://$baseDomain/top/200"
        val respString = RestClient.get(moviesUrl).body!!.string()
        val response = StringUtils.removeNewLinesAndMultipleSpaces(respString)
        val results = MOVIE_REGEX.findAll(response)
        println("Total movies found : ${results.toList().size}")
        val baseMovies = mutableListOf<BaseMovie>()
        for (result in results) {
            val movieName = result.groups["movieName"]!!.value
            val detailsSubPath = result.groups["link"]!!.value.replace("https://$baseDomain", "")
            baseMovies.add(BaseMovie(movieName, detailsSubPath))
        }
        return baseMovies
    }


    /**
     * To get thepiratebay proxy domains
     */
    fun getProxies(): List<String> {
        return listOf("thepiratebay10.org")
        /*

        COMMENTED DUE TO AN ISSUE WITH https://thepiratebay-proxylist.org/ (no domains available)

        println("Collecting proxies from $PROXIES_URL...")
        val resp = RestClient.get(PROXIES_URL).body!!.string()
        val results = PROXY_REGEX.findAll(resp)
        val proxies = mutableListOf<String>("thepiratebay10.org") // with default one
        for (result in results) {
            proxies.add(result.groups["domain"]!!.value)
        }
        println("Proxies : $proxies")

        return proxies*/
    }

    override fun getIssueId(): Int? = 1

    override fun parseMovie(baseDomain: String, newBaseMovie: BaseMovie): Movie {
        // Getting TPB data
        val tpbUrl = "https://$baseDomain${newBaseMovie.subPath}"
        val tpbResponse = RestClient.get(tpbUrl).body!!.string()
        val tpbParser = TPBParser(tpbResponse)
        val magnetLink = tpbParser.getMagnetLink()
        val fileCount = tpbParser.getFileCount()
        val fileSize = tpbParser.getFileSize()
        val languages = tpbParser.getLanguages()
        val infoUrl = tpbParser.getInfoUrl()

        // IMDB data
        var title: String? = null
        var imdbRating: Float? = null
        var imdbVoteCount: Long? = null
        var category: String? = null
        var duration: String? = null
        var releaseDate: String? = null
        var plot: String? = null
        var trailerUrl: String? = null
        var imdbId: String? = null
        var year: Int? = null
        var posterUrl: String? = null
        var actors: List<IMDBResponse.Actor>? = null
        var directors: List<IMDBResponse.Director>? = null

        val isImdbUrl = infoUrl?.contains("imdb.com/title/") ?: false

        if (isImdbUrl) {
            // Getting IMDB data
            imdbId = tpbParser.parseImdbId(infoUrl!!)
            println("Collecting more details from $infoUrl")
            val imdbResponse = RestClient.get(infoUrl).body!!.string()
            val imdbParser = IMDBParser(imdbResponse)
            title = imdbParser.getTitle()
            imdbRating = imdbParser.getRating()
            imdbVoteCount = imdbParser.getRatingVoteCount()
            category = imdbParser.getCategory()
            duration = imdbParser.getDuration()
            releaseDate = imdbParser.getReleaseDate()
            plot = imdbParser.getPlot()
            trailerUrl = imdbParser.getTrailerUrl()
            year = imdbParser.getYear()
            posterUrl = imdbParser.getPosterUrl()
            actors = imdbParser.getActors()
            directors = imdbParser.getDirectors()
        }

        println("Movie collected : $title")

        return Movie(
            newBaseMovie.baseName,
            newBaseMovie.subPath,
            tpbUrl,
            imdbId,
            infoUrl,
            title,
            magnetLink,
            fileCount,
            fileSize,
            languages,
            imdbRating,
            imdbVoteCount,
            category,
            duration,
            year,
            releaseDate,
            plot,
            trailerUrl,
            posterUrl,
            actors,
            directors,
            null,
            null
        )
    }

}