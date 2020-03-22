package com.theapache64.moviemonk.core.engines

import com.theapache64.moviemonk.core.interfaces.Engine
import com.theapache64.moviemonk.core.interfaces.MovieProvider
import com.theapache64.moviemonk.data.CreatedMoviesRepository
import com.theapache64.moviemonk.data.base.BasePostedMoviesRepository
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.models.external.github.Issue
import com.theapache64.moviemonk.utils.GitHubAPI
import java.io.IOException

object CreatorEngine : Engine {

    private const val AUTHOR_NAME = "theapache64"
    private const val REPO_NAME = "movie-monk-creator"

    override fun handle(movieProvider: MovieProvider, newMovies: List<Movie>) {
        for (movie in newMovies) {
            try {
                println("--------------------------------------")
                val movieTitle = movie.title ?: movie.baseName
                val movieYear = if (movie.year != null) {
                    "(${movie.year})"
                } else {
                    ""
                }
                val issueTitle = "$movieTitle $movieYear"
                val issue = getIssueByTitle(issueTitle)
                var movieBody = prepareMovieBody(movie)
                if (issue != null) {

                    // movie already exist, so add a comment

                    GitHubAPI.createIssueComment(
                        AUTHOR_NAME,
                        REPO_NAME,
                        issue.number,
                        movieBody
                    )


                    println("Added comment")

                } else {

                    val typeLabel = if (!isSeries(movie.baseName)) {
                        "movie"
                    } else {
                        "other"
                    }

                    val labels = mutableListOf<String>().apply {
                        add(typeLabel)

                        if (movie.year != null) {
                            add("year-${movie.year}")
                        }

                        if (movie.genre != null) {
                            movie.genre.split(",").forEach { genre ->
                                add("genre-${genre.trim().toLowerCase()}")
                            }
                        }

                        if (movie.actors != null) {
                            movie.actors.forEach { actor ->
                                add("actor-${toLabel(actor.name!!)}")
                            }
                        }

                        if (movie.directors != null) {
                            movie.directors.forEach { director ->
                                add("director-${toLabel(director.name!!)}")
                            }
                        }
                    }

                    if (movie.posterUrl != null) {
                        movieBody = "![poster](${movie.posterUrl})\n$movieBody"
                    }

                    // Create the issue
                    val createIssueResp = GitHubAPI.createIssue(
                        AUTHOR_NAME,
                        REPO_NAME,
                        issueTitle,
                        movieBody,
                        labels
                    )

                    println("Created issue : ${createIssueResp.number}")

                    GitHubAPI.lockIssue(
                        AUTHOR_NAME,
                        REPO_NAME,
                        createIssueResp.number
                    )

                    println("Issue locked")
                }
                CreatedMoviesRepository.add(BaseMovie(movie.baseName, movie.baseUrl))
                println("Movie added to cache...")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun toLabel(name: String) =
        name.toLowerCase().replace(" ", "-")

    private val SERIES_REGEX = "S\\d+(E\\d+)?".toRegex()

    private fun isSeries(movieBaseName: String): Boolean {
        return SERIES_REGEX.find(movieBaseName) != null
    }

    private fun getIssueByTitle(issueTitle: String): Issue? {
        return GitHubAPI.getIssues(
            AUTHOR_NAME,
            REPO_NAME
        ).find { it.title == issueTitle }
    }

    override fun getRepo(): BasePostedMoviesRepository = CreatedMoviesRepository

    override fun prepareMovieBody(movie: Movie): String {

        val sb = StringBuilder()

        sb.append("\n\n☠️ TPB :  [${movie.baseName}](${movie.baseUrl})")

        if (movie.title != null) {
            sb.append("\n\n🎥 [${movie.title} (${movie.year})](${movie.infoUrl})")
        }

        if (movie.imdbRating != null && movie.imdbVoteCount != null) {
            sb.append("\n\n🌟 IMDB : **${movie.imdbRating}**/10 , Total Votes : **${movie.imdbVoteCount}**")
        }

        if (movie.genre != null) {
            sb.append("\n\n🎎 Genre : **${movie.genre}**")
        }

        if (movie.duration != null) {
            sb.append("\n\n⌚ Duration: **${movie.duration}**")
        }

        if (movie.releaseDate != null) {
            sb.append("\n\n📆 Release Date: **${movie.releaseDate}**")
        }

        if (movie.trailerUrl != null) {
            sb.append("\n\n\uD83C\uDF9E Trailer: [Watch](${movie.trailerUrl})")
        }


        if (movie.languages != null) {
            sb.append("\n\n🙊 Language : **${movie.languages}**")
        }

        if (movie.fileSize != null) {
            sb.append("\n\n🗃 Size: **${movie.fileSize}**, File Count : **${movie.fileCount}**")
        }

        if (movie.plot != null) {
            sb.append("\n\n❇ *${movie.plot}*")
        }

        if (movie.magnetLink != null) {
            sb.append("\n> ${movie.magnetLink}\n\n")
        }

        return sb.toString()
    }
}