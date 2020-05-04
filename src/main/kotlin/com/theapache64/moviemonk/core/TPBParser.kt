package com.theapache64.moviemonk.core

import com.theapache64.moviemonk.utils.StringUtils
import java.io.File

class TPBParser(
    private var htmlResponse: String
) {

    companion object {
        private val MAGNET_REGEX = "href=\"(?<magnetUrl>magnet:.+?)\" title=\"Get this torrent".toRegex()
        private val FILE_COUNT_REGEX = "<dt>Files:<\\/dt> <dd><a href=\".+\">(?<fileCount>\\d+)<\\/a><\\/dd>".toRegex()
        private val FILE_SIZE_REGEX = "<dt>Size:<\\/dt> <dd>(?<fileSize>.+) \\(\\d+ Bytes\\)<\\/dd>".toRegex()
        private val INFO_URL_REGEX = "<dt>Info:<\\/dt> <dd><a href=\"(?<infoUrl>.+?)\"".toRegex()
        private val SPOKEN_LANGUAGES_REGEX = "<dt>Spoken language\\(s\\):<\\/dt> <dd>(?<languages>.+?)<\\/dd>".toRegex()
        private val IMDB_ID_REGEX = "https\\:\\/\\/www\\.imdb\\.com\\/title\\/(?<imdbId>\\w+\\d+)".toRegex()
        private val SUPER_INFO_REGEX = "(?<infoUrl>https:\\/\\/(?:www\\.)?imdb\\.com\\/title\\/tt\\d+)".toRegex()
    }

    init {
        htmlResponse = StringUtils.removeNewLinesAndMultipleSpaces(htmlResponse)
            .replace("&nbsp;", " ")
    }

    fun getMagnetLink(): String? {
        return StringUtils.getFromPattern(
            htmlResponse,
            MAGNET_REGEX, "magnetUrl"
        ).firstOrNull()
    }

    fun getFileCount(): Int? {
        return StringUtils.getFromPattern(
            htmlResponse,
            FILE_COUNT_REGEX, "fileCount"
        ).firstOrNull()?.toInt()
    }

    fun getFileSize(): String? {
        return StringUtils.getFromPattern(
            htmlResponse,
            FILE_SIZE_REGEX, "fileSize"
        ).firstOrNull()?.trim()
    }

    fun getInfoUrl(): String? {
        var infoUrl = StringUtils.getFromPattern(
            htmlResponse,
            INFO_URL_REGEX, "infoUrl"
        ).firstOrNull()

        if (infoUrl == null) {
            infoUrl = StringUtils.getFromPattern(
                htmlResponse,
                SUPER_INFO_REGEX,
                "infoUrl"
            ).firstOrNull()
        }

        return infoUrl
    }

    fun getLanguages(): String? {
        return StringUtils.getFromPattern(
            htmlResponse,
            SPOKEN_LANGUAGES_REGEX, "languages"
        ).firstOrNull()
    }

    fun parseImdbId(imdbUrl: String): String? {
        return IMDB_ID_REGEX.find(imdbUrl)?.groups?.get("imdbId")?.value
    }
}