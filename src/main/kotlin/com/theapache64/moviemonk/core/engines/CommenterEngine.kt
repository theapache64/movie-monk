package com.theapache64.moviemonk.core.engines

import com.theapache64.moviemonk.core.interfaces.Engine
import com.theapache64.moviemonk.core.interfaces.MovieProvider
import com.theapache64.moviemonk.data.CommentedMoviesRepository
import com.theapache64.moviemonk.data.base.BasePostedMoviesRepository
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.utils.GitHubAPI
import java.io.IOException

/**
 * To comment on the given issue id
 */
object CommenterEngine : Engine {

    private const val AUTHOR_NAME = "theapache64"
    private const val REPO_NAME = "movie-monk-commenter"
    private const val MAX_MOVIES_PER_COMMENT = 10

    override fun handle(movieProvider: MovieProvider, newMovies: List<Movie>) {
        val moviesChunks = newMovies.chunked(MAX_MOVIES_PER_COMMENT)
        val issueId = movieProvider.getIssueId()!!
        for (moviesChunk in moviesChunks) {

            val commentBody = StringBuilder()

            for (movie in moviesChunk) {
                val comment = prepareMovieBody(movie)
                commentBody.append(comment)
            }

            try {
                GitHubAPI.createIssueComment(
                    AUTHOR_NAME,
                    REPO_NAME,
                    issueId,
                    commentBody.toString()
                )

                CommentedMoviesRepository.addAll(moviesChunk.map { BaseMovie(it.baseName, it.baseUrl) })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    override fun getRepo(): BasePostedMoviesRepository = CommentedMoviesRepository

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

        sb.append("\n\n↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩")

        return sb.toString()

    }
}