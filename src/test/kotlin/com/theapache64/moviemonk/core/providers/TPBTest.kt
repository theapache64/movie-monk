package com.theapache64.moviemonk.core.providers

import com.winterbe.expekt.should
import org.junit.Test

internal class TPBTest {

    @Test
    fun getProxies() {
        TPB.getProxies().size.should.above(1)
    }
}