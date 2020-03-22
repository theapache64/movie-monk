package com.theapache64.moviemonk.utils

import com.theapache64.moviemonk.models.external.telegram.SendMessageRequest
import com.theapache64.moviemonk.models.external.telegram.SendMessageResponse
import com.theapache64.moviemonk.models.external.telegram.SendPhotoRequest
import com.theapache64.moviemonk.models.external.telegram.SendPhotoResponse
import java.io.IOException

object TelegramAPI {

    private const val BASE_URL = "https://api.telegram.org"

    @Throws(IOException::class)
    fun sendPhoto(
        from: String,
        to: String,
        photoUrl: String,
        replyMsgId: Long? = null
    ): SendPhotoResponse {

        val url = "$BASE_URL/bot$from/sendPhoto"
        val response = RestClient.post(
            url,
            null,
            SendPhotoRequest(
                to,
                photoUrl,
                replyMsgId
            )
        )
        val respJsonString = response.body!!.string()
        if (response.code != 200) {
            throw IOException("Failed to send photo : $photoUrl -> '$respJsonString'")
        }
        return GsonUtil.gson.fromJson(respJsonString, SendPhotoResponse::class.java)
    }

    /**
     * To send a text with Markdown
     */
    @Throws(IOException::class)
    fun sendHtmlMessage(
        from: String,
        to: String,
        message: String,
        replyMsgId: Long?
    ): SendMessageResponse {

        val url = "$BASE_URL/bot$from/sendMessage"

        val response = RestClient.post(
            url,
            null,
            SendMessageRequest(
                to,
                message,
                true,
                "HTML",
                replyMsgId
            )
        )

        val respJsonString = response.body!!.string()
        if (response.code != 200) {
            throw IOException("Failed to send message '$message' -> $respJsonString")
        }
        return GsonUtil.gson.fromJson(respJsonString, SendMessageResponse::class.java)
    }
}