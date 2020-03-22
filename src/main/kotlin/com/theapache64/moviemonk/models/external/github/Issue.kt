package com.theapache64.moviemonk.models.external.github

import com.google.gson.annotations.SerializedName


data class Issue(
    @SerializedName("number")
    val number: Int,
    @SerializedName("title")
    val title: String
)