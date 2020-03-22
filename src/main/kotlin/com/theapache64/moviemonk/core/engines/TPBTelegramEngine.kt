package com.theapache64.moviemonk.core.engines

object TPBTelegramEngine : BaseTelegramEngine() {
    override fun getChannelName(): String = "@th3_pirate_bay"
}