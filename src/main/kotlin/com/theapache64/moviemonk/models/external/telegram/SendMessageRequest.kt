package com.theapache64.moviemonk.models.external.telegram

import com.google.gson.annotations.SerializedName


data class SendMessageRequest(
    @SerializedName("chat_id")
    val chatId: String, // to
    @SerializedName("text")
    val text: String, // This is some message
    @SerializedName("disable_web_page_preview")
    val isDisableWebPagePreview: Boolean?,
    @SerializedName("parse_mode")
    val parseMode: String?,
    @SerializedName("reply_to_message_id")
    val replyMsgId: Long?,
    @SerializedName("reply_markup")
    val replyMarkUp: ReplyMarkUp? = null
) {
    class ReplyMarkUp(
        @SerializedName("inline_keyboard")
        val inlineKeyboard: List<List<InlineKeyboardButton>>? = null
    )

    class InlineKeyboardButton(
        @SerializedName("text")
        val text: String,
        @SerializedName("url")
        val url: String? = null
    )

}