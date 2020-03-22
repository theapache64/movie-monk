package com.theapache64.moviemonk.models.external.github

import com.google.gson.annotations.SerializedName

class AddCommentRequest(
    @SerializedName("body")
    private val body: String
)