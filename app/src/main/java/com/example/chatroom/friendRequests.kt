package com.example.chatroom

data class friendRequests(
    val docId: String,
    val senderEmail: String,
    val receiverEmail: String,
    val requestSent: Boolean,
    val requestAccepted: Boolean = false
)

