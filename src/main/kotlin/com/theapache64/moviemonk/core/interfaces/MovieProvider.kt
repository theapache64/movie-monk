package com.theapache64.moviemonk.core.interfaces

import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.models.Movie

interface MovieProvider {
    fun getIssueId(): Int?
    fun getTrendingMovies(baseDomain: String): List<BaseMovie>
    fun getBaseDomain(): String
    fun parseMovie(baseDomain: String, newBaseMovie: BaseMovie): Movie
}