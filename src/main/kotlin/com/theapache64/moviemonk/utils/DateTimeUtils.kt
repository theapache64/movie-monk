package com.theapache64.moviemonk.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    private val DATE_FORMAT = SimpleDateFormat("MMM dd yyyy")
    private val YEAR_FORMAT = SimpleDateFormat("yyyy")

    fun getDateFormatted(date: Date): String {
        return DATE_FORMAT.format(date)
    }

    fun getYear(date: Date): Int {
        return YEAR_FORMAT.format(date).toInt()
    }
}