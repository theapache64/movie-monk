package com.theapache64.moviemonk.core.interfaces

import com.theapache64.moviemonk.data.base.BasePostedMoviesRepository
import com.theapache64.moviemonk.models.Movie

interface Engine {
    fun handle(movieProvider: MovieProvider, newMovies: List<Movie>)
    fun getRepo(): BasePostedMoviesRepository
    fun prepareMovieBody(movie: Movie): String
}