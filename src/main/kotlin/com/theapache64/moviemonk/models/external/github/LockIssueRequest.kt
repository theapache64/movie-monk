package com.theapache64.moviemonk.models.external.github

import com.google.gson.annotations.SerializedName


data class LockIssueRequest(
    @SerializedName("active_lock_reason")
    val activeLockReason: String, // too heated
    @SerializedName("locked")
    val locked: Boolean // true
)