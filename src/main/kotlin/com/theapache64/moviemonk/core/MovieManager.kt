package com.theapache64.moviemonk.core

import com.theapache64.moviemonk.core.providers.DVDPlay
import com.theapache64.moviemonk.core.providers.TPB
import com.theapache64.moviemonk.data.MoviesRepository
import com.theapache64.moviemonk.data.base.BasePostedMoviesRepository
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie

object MovieManager {

    fun filterNewMovies(repo: BasePostedMoviesRepository, trendingMovies: List<BaseMovie>): List<BaseMovie> {
        val postedMovies = repo.getAll()
        return trendingMovies.filter { trendingMovie ->
            postedMovies.find { oldMovie ->
                oldMovie.subPath == trendingMovie.subPath
            } == null
        }
    }

    fun getFullMovies(baseDomain: String, newMovies: List<BaseMovie>): List<Movie> {
        val movieList = mutableListOf<Movie>()
        for (baseMovie in newMovies) {
            println("↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↪↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩↩")
            println("Getting movie details for ${baseMovie.baseName}")
            println("Finding local repo for `${baseMovie.baseName}` and `${baseMovie.subPath}`")
            var movie = MoviesRepository.findBy(baseMovie)
            if (movie == null) {
                println("but not available")
                // not available in cache so getting from live
                movie = getMovie(baseDomain, baseMovie)
                MoviesRepository.add(movie)
            } else {
                println("yes it's available")
            }

            movieList.add(movie)
        }
        return movieList
    }

    private fun getMovie(baseDomain: String, newBaseMovie: BaseMovie): Movie {

        return if (baseDomain == DVDPlay.BASE_URL) {
            DVDPlay.parseMovie(baseDomain, newBaseMovie)
        } else {
            TPB.parseMovie(baseDomain, newBaseMovie)
        }
    }


}