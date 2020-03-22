package com.theapache64.moviemonk.models

data class BaseMovie(
    val baseName: String,
    val subPath: String,
    val movie: Movie? = null
)