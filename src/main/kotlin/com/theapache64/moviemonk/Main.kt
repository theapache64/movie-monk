package com.theapache64.moviemonk

import com.theapache64.moviemonk.core.MovieManager
import com.theapache64.moviemonk.core.engines.CommenterEngine
import com.theapache64.moviemonk.core.engines.CreatorEngine
import com.theapache64.moviemonk.core.engines.DVDPlayTelegramEngine
import com.theapache64.moviemonk.core.engines.TPBTelegramEngine
import com.theapache64.moviemonk.core.providers.DVDPlay
import com.theapache64.moviemonk.core.providers.TPB

class Main

fun main(args: Array<String>) {

    val providers = mapOf(
        TPB to listOf(
            CreatorEngine,
            CommenterEngine,
            TPBTelegramEngine
        ),
        DVDPlay to listOf(
            DVDPlayTelegramEngine
        )
    )

    for (provider in providers) {

        val movieProvider = provider.key
        val baseDomain = movieProvider.getBaseDomain()

        for (engine in provider.value) {

            println("------------------------")
            println("Starting ${engine.javaClass.simpleName} engine...")

            val trendingMovies = movieProvider.getTrendingMovies(baseDomain)

            if (trendingMovies.isNotEmpty()) {

                val newMovies = MovieManager.filterNewMovies(engine.getRepo(), trendingMovies)
                println("Total new movies : ${newMovies.size}")
                if (newMovies.isNotEmpty()) {
                    val newFullMovies = MovieManager.getFullMovies(baseDomain, newMovies)

                    require(newMovies.size == newFullMovies.size) {
                        "Something went wrong while converting base movies to full movies"
                    }

                    engine.handle(movieProvider, newFullMovies)

                } else {
                    println("No new movies found")
                }

            } else {
                println("Failed to get latest movies")
            }
        }
    }
}