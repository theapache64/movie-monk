package com.theapache64.moviemonk.models

import com.theapache64.moviemonk.models.external.imdb.IMDBResponse

data class Movie(
    val baseName: String,
    val baseUrl: String,
    val fullBaseUrl : String,
    val imdbId: String? = null,
    val infoUrl: String? = null,
    val title: String? = null,
    val magnetLink: String? = null,
    val fileCount: Int? = null,
    val fileSize: String? = null,
    val languages: String? = null,
    val imdbRating: Float? = null,
    val imdbVoteCount: Long? = null,
    val genre: String? = null,
    val duration: String? = null,
    val year: Int? = null,
    val releaseDate: String? = null,
    val plot: String? = null,
    val trailerUrl: String? = null,
    val posterUrl: String? = null,
    val actors: List<IMDBResponse.Actor>? = null,
    val directors: List<IMDBResponse.Director>? = null,
    val quality: String? = null,
    val downloads: List<Download>? = null,
    val screensUrls: List<String>? = null
) {
    data class Download(
        val source: String,
        val url: String,
        val title: String
    )
}