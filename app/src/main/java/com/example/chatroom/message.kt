package com.example.chatroom

import com.google.firebase.Timestamp

data class MessageInfo(
    val receiver: String,
    val sender: String,
    val message: String,
    val timestamp: Long,
    val randomKey: String
)

data class MessageDtl(
    val message: String="",
    val sender: String="",
    val receiver: String="",
    val timestamp: Long = 0,
    val randomKey: String = ""
)

data class MessageTimeStampString(
    val message: String="",
    val sender: String="",
    val receiver: String="",
    val timestamp: String = "",
    val randomKey: String = ""
)