package com.theapache64.moviemonk.models.external.github

import com.google.gson.annotations.SerializedName


data class CreateIssueRequest(
    @SerializedName("body")
    val body: String, // I'm having a problem with this.
    @SerializedName("labels")
    val labels: List<String>,
    @SerializedName("title")
    val title: String // Found a bug
)