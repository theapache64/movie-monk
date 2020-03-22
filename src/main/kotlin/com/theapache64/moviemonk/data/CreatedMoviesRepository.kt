package com.theapache64.moviemonk.data

import com.theapache64.moviemonk.data.base.BasePostedMoviesRepository
import com.theapache64.moviemonk.utils.JarUtils
import java.io.File

object CreatedMoviesRepository : BasePostedMoviesRepository() {
    override fun getMoviesFile(): File = File("${JarUtils.getJarDir()}created_movies.json")
}