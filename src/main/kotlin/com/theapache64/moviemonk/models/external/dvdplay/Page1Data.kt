package com.theapache64.moviemonk.models.external.dvdplay

class Page1Data(
    val posterUrl: String,
    val screensUrls: List<String>,
    val innerPageUrls: List<InnerPageUrl>
)

class InnerPageUrl(
    val title: String,
    val url: String
)