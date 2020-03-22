package com.theapache64.moviemonk.models.external.telegram

import com.google.gson.annotations.SerializedName

class SendPhotoRequest(
    @SerializedName("chat_id")
    val to: String,
    @SerializedName("photo")
    val url: String,
    @SerializedName("reply_to_message_id")
    val replyMsgId: Long?
)