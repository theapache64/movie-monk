package com.theapache64.moviemonk.data

import com.google.gson.reflect.TypeToken
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie
import com.theapache64.moviemonk.utils.GsonUtil
import com.theapache64.moviemonk.utils.JarUtils
import java.io.File

object MoviesRepository {

    private val IMDB_MOVIES_JSON = File("${JarUtils.getJarDir()}movies.json")
    private val type = object : TypeToken<List<Movie>>() {}.type

    fun findBy(newMovie: BaseMovie): Movie? {
        val allMovies = getAll()
        return allMovies.find { movie -> movie.baseName == newMovie.baseName && movie.baseUrl == newMovie.subPath }
    }

    private fun getAll(): List<Movie> {
        return GsonUtil.gson.fromJson(IMDB_MOVIES_JSON.readText(), type)
    }

    fun add(movie: Movie) {
        val curMovies = getAll()
        val allMovies = mutableListOf(*curMovies.toTypedArray(), movie)
        val allMoviesJson = GsonUtil.gson.toJson(allMovies)
        IMDB_MOVIES_JSON.writeText(allMoviesJson)
    }

}