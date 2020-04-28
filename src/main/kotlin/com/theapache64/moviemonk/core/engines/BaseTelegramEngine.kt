package com.theapache64.moviemonk.core.engines

import com.theapache64.moviemonk.core.interfaces.Engine
import com.theapache64.moviemonk.core.interfaces.MovieProvider
import com.theapache64.moviemonk.data.TelegramMoviesRepository
import com.theapache64.moviemonk.data.base.BasePostedMoviesRepository
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.utils.TelegramAPI
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseTelegramEngine : Engine {

    abstract fun getChannelName(): String

    private val botToken: String = System.getenv("MOVIE_MONK_TELEGRAM_BOT_TOKEN")

    override fun handle(movieProvider: MovieProvider, newMovies: List<Movie>) {

        for (movie in newMovies) {

            try {
                val msg = prepareMovieBody(movie)
                println("Posting to telegram : ${movie.title}")

                // Sending photo first
                var photoId: Long? = null
                if (movie.posterUrl != null) {
                    println("Sending poster...")

                    try {
                        photoId = TelegramAPI.sendPhoto(
                            botToken,
                            getChannelName(),
                            movie.posterUrl
                        ).result.messageId

                        Thread.sleep(2000)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        println("Skipping poster...")
                    }

                }

                // Sending screens
                if (movie.screensUrls != null) {
                    for (screenUrl in movie.screensUrls) {
                        try {
                            TelegramAPI.sendPhoto(
                                botToken,
                                getChannelName(),
                                screenUrl,
                                photoId
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                            println("Skipping screen...")
                        }

                        Thread.sleep(2000)
                    }
                }


                // Sending movie details
                val msgId = TelegramAPI.sendHtmlMessage(
                    botToken,
                    getChannelName(),
                    msg,
                    photoId
                ).result.messageId

                // Sending download links
                if (movie.downloads != null) {
                    for (download in movie.downloads) {

                        val downloadMsg = "📥 ${download.title}"
                        println("Sending link to telegram : $download")
                        try {

                            TelegramAPI.sendLinkButton(
                                botToken,
                                getChannelName(),
                                download.source,
                                downloadMsg,
                                download.url,
                                msgId
                            )

                            Thread.sleep(2000)
                        } catch (e: SocketTimeoutException) {
                            e.printStackTrace()
                            println("Timeout hit! cancelling $downloadMsg")
                        }
                    }
                }

                // Sending magnet link
                if (movie.magnetLink != null) {

                    TelegramAPI.sendHtmlMessage(
                        botToken,
                        getChannelName(),
                        movie.magnetLink,
                        msgId
                    )

                    Thread.sleep(2000)
                }


                TelegramMoviesRepository.add(BaseMovie(movie.baseName, movie.baseUrl))

            } catch (e: IOException) {
                e.printStackTrace()
                println("Skipping movie ${movie.baseName}")
            }
        }
    }

    override fun getRepo(): BasePostedMoviesRepository = TelegramMoviesRepository

    override fun prepareMovieBody(movie: Movie): String {

        val sb = StringBuilder()

        val fullLinkWithMovieName = "<a href=\"${movie.fullBaseUrl}\">${movie.baseName}</a>"
        sb.append("\n\n \uD83D\uDC49 $fullLinkWithMovieName")

        if (movie.title != null) {
            val infoUrlLink = "<a href=\"${movie.infoUrl}\">${movie.title} (${movie.year})</a>"
            sb.append("\n\n🎥 $infoUrlLink")
        }

        if (movie.imdbRating != null && movie.imdbVoteCount != null) {
            sb.append("\n\n🌟 IMDB : <b>${movie.imdbRating}</b>/10 , Total Votes : <b>${movie.imdbVoteCount}</b>")
        }

        if (movie.genre != null) {
            sb.append("\n\n🎎 Genre : <b>${movie.genre}</b>")
        }

        if (movie.duration != null) {
            sb.append("\n\n⌚ Duration: <b>${movie.duration}</b>")
        }

        if (movie.releaseDate != null) {
            sb.append("\n\n📆 Release Date: <b>${movie.releaseDate}</b>")
        }

        if (movie.trailerUrl != null) {
            val trailerLink = "<a href=\"${movie.trailerUrl}\">Watch</a>"
            sb.append("\n\n\uD83C\uDF9E Trailer: $trailerLink")
        }


        if (movie.languages != null) {
            sb.append("\n\n🙊 Language : <b>${movie.languages}</b>")
        }

        if (movie.fileSize != null) {
            sb.append("\n\n🗃 Size: <b>${movie.fileSize}</b>, File Count : <b>${movie.fileCount}</b>")
        }

        if (movie.plot != null) {
            sb.append("\n\n❇ <b>${movie.plot}</b>")
        }

        return sb.toString()

    }
}