package com.theapache64.moviemonk.data.base

import com.google.gson.reflect.TypeToken
import com.theapache64.moviemonk.models.BaseMovie
import com.theapache64.moviemonk.utils.GsonUtil
import java.io.File

abstract class BasePostedMoviesRepository {

    abstract fun getMoviesFile(): File

    private val type = object : TypeToken<List<BaseMovie>>() {}.type

    /**
     * To get all movies that are handled by engines
     */
    fun getAll(): List<BaseMovie> {
        val postedMoviesJson = getMoviesFile().readText()
        return GsonUtil.gson.fromJson(postedMoviesJson, type)
    }

    fun addAll(moviesChunk: List<BaseMovie>) {
        val x = getAll().toMutableList()
        x.addAll(moviesChunk)
        val json = GsonUtil.gson.toJson(x)
        getMoviesFile().writeText(json)
    }

    fun add(movie: BaseMovie) {
        val x = getAll().toMutableList()
        x.add(movie)
        val json = GsonUtil.gson.toJson(x)
        getMoviesFile().writeText(json)
    }
}