package com.theapache64.moviemonk.utils

import com.winterbe.expekt.should
import org.junit.Test


internal class StringUtilsTest {

    @Test
    fun getFromPattern() {
        StringUtils.getFromPattern(
            "This is sample",
            "This is (?<value>.+)".toRegex(),
            "value"
        ).first().should.equal("sample")
    }

    @Test
    fun getProper() {
        StringUtils.getProper(-1, "movie", "movies").should.equal("movie")
        StringUtils.getProper(0, "movie", "movies").should.equal("movie")
        StringUtils.getProper(1, "movie", "movies").should.equal("movie")
        StringUtils.getProper(2, "movie", "movies").should.equal("movies")
        StringUtils.getProper(100, "movie", "movies").should.equal("movies")
    }

    @Test
    fun removeNewLinesAndMultipleSpaces() {
        StringUtils.removeNewLinesAndMultipleSpaces(
            """
                This is 
                some data with          more space
            """.trimIndent()
        ).should.equal("This is some data with more space")
    }
}