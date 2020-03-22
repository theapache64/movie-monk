package com.theapache64.moviemonk.utils

import com.google.gson.reflect.TypeToken
import com.theapache64.moviemonk.models.external.imdb.IMDBResponse
import java.text.SimpleDateFormat

class IMDBParser(
    private val response: String
) {

    companion object {
        private val DURATION_REGEX = "PT((?<hours>\\d+)H)?((?<minutes>\\d+)M)?".toRegex()
        private val DATE_PUBLISHED_FORMAT = SimpleDateFormat("yyyy-MM-dd")
    }

    private val data: IMDBResponse

    init {
        val json = response.split("<script type=\"application/ld+json\">")[1].split("</script>")[0]
        data = GsonUtil.gson.fromJson(json, IMDBResponse::class.java)
    }

    fun getTitle(): String? {
        return data.name
    }

    fun getRating(): Float? {
        return data.aggregateRating?.ratingValue?.toFloat()
    }

    fun getRatingVoteCount(): Long? {
        return data.aggregateRating?.ratingCount
    }

    fun getDuration(): String? {
        return parseDuration(data.duration)
    }

    private fun parseDuration(duration: String?): String? {
        if (duration != null) {
            val matcher = DURATION_REGEX.find(duration)
            if (matcher != null) {
                val hours = matcher.groups["hours"]?.value?.toInt()
                val minutes = matcher.groups["minutes"]?.value?.toInt()

                val hrs = "$hours ${StringUtils.getProper(hours, "hour", "hours")}"
                val mins = "$minutes ${StringUtils.getProper(minutes, "minute", "minutes")}"

                return when {
                    hours != null && minutes != null -> {
                        "$hrs $mins"
                    }

                    hours != null -> {
                        hrs
                    }
                    else -> {
                        mins
                    }
                }
            }
        }

        return null
    }

    fun getCategory(): String? {
        return when (data.genre) {
            is String -> {
                data.genre
            }
            is List<*> -> {
                data.genre.joinToString()
            }
            else -> {
                null
            }
        }
    }

    fun getReleaseDate(): String? {
        if (data.datePublished != null) {
            return DateTimeUtils.getDateFormatted(DATE_PUBLISHED_FORMAT.parse(data.datePublished))
        }
        return null
    }

    fun getPlot(): String? {
        return data.description
    }

    fun getTrailerUrl(): String? {
        if (data.trailer?.embedUrl != null) {
            return "https://imdb.com${data.trailer.embedUrl}"
        }
        return null
    }

    fun getYear(): Int? {
        if (data.datePublished != null) {
            return DateTimeUtils.getYear(DATE_PUBLISHED_FORMAT.parse(data.datePublished))
        }

        return null
    }

    fun getPosterUrl(): String? {
        return data.image
    }

    fun getActors(): List<IMDBResponse.Actor>? {
        if (data.actor != null) {
            val actorJson = GsonUtil.gson.toJson(data.actor)
            return if (data.actor is List<*>) {
                val type = object : TypeToken<List<IMDBResponse.Actor>>() {}.type
                GsonUtil.gson.fromJson(actorJson, type)
            } else {
                return listOf(
                    GsonUtil.gson.fromJson(actorJson, IMDBResponse.Actor::class.java)
                )
            }
        }
        return null
    }

    fun getDirectors(): List<IMDBResponse.Director>? {
        if (data.director != null) {
            val directorJson = GsonUtil.gson.toJson(data.director)
            return if (data.director is List<*>) {
                val type = object : TypeToken<List<IMDBResponse.Director>>() {}.type
                GsonUtil.gson.fromJson(directorJson, type)
            } else {
                return listOf(
                    GsonUtil.gson.fromJson(directorJson, IMDBResponse.Director::class.java)
                )
            }
        }
        return null
    }

}