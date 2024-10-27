package com.example.chatroom

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [dataToCache::class],
    version = 1,
    exportSchema = false
)

abstract class dataDatabase: RoomDatabase(){
    abstract fun dataDao(): dataDao
}