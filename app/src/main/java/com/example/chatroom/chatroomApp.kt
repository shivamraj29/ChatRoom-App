package com.example.chatroom

import android.app.Application

class chatroomApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}