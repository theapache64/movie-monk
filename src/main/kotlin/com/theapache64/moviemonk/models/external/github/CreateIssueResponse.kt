package com.theapache64.moviemonk.models.external.github

import com.google.gson.annotations.SerializedName

class CreateIssueResponse(
    @SerializedName("number")
    val number: Int
)