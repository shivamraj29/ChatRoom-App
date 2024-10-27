package com.example.chatroom

import android.content.Context
import androidx.room.Room

object Graph {

    lateinit var database: dataDatabase

    val dataRepository by lazy {
        dataRespository(dataDao = database.dataDao())
    }

    fun provide(context:Context){
    database = Room.databaseBuilder(context, dataDatabase::class.java, "userInfo.db").build()
    }
}