package com.theapache64.moviemonk.utils

import org.junit.Test

class TelegramAPITest {

    val BOT_TOKEN: String = System.getenv("MOVIE_MONK_TELEGRAM_BOT_TOKEN")

    @Test
    fun test() {

        //(url= https://sharer.pw/file/Mzw9Gz1y9u3 , title=[HDRip - 720p - x264 - AAC - 1.2GB - HC-ESub])
        TelegramAPI
            .sendMarkdownMessage(
                BOT_TOKEN,
                "@dvd_play",
                "[HDRip - 720p - x264 - AAC - 1.2GB - HC-ESub](https://sharer.pw/file/Mzw9Gz1y9u3)",
                null
            )
    }
}