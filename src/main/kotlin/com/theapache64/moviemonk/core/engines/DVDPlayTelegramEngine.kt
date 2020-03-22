package com.theapache64.moviemonk.core.engines

object DVDPlayTelegramEngine : BaseTelegramEngine() {
    override fun getChannelName(): String = "@dvd_play"
}