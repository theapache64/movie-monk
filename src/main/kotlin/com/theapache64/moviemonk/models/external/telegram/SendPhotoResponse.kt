package com.theapache64.moviemonk.models.external.telegram

import com.google.gson.annotations.SerializedName


data class SendPhotoResponse(
    @SerializedName("ok")
    val ok: Boolean, // true
    @SerializedName("result")
    val result: Result
) {
    data class Result(
        @SerializedName("chat")
        val chat: Chat,
        @SerializedName("date")
        val date: Long, // 1584216008
        @SerializedName("message_id")
        val messageId: Long, // 145
        @SerializedName("photo")
        val photo: List<Photo>
    ) {
        data class Chat(
            @SerializedName("id")
            val id: Long, // -1001423106120
            @SerializedName("title")
            val title: String, // Movie Monk
            @SerializedName("type")
            val type: String, // channel
            @SerializedName("username")
            val username: String // movie_m0nk
        )

        data class Photo(
            @SerializedName("file_id")
            val fileId: String, // AgACAgQAAx0EVNLgSAADkV5tN8jIkJ1ZXF0MuwtkOMDc5jJ9AAJGqzEbgkRtU5IuO4l1619_72aoGwAEAQADAgADbQADeyMJAAEYBA
            @SerializedName("file_size")
            val fileSize: Int, // 9549
            @SerializedName("file_unique_id")
            val fileUniqueId: String, // AQAD72aoGwAEeyMJAAE
            @SerializedName("height")
            val height: Int, // 300
            @SerializedName("width")
            val width: Int // 200
        )
    }
}