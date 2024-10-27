package com.example.chatroom

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("userData-table")
data class dataToCache(
    @PrimaryKey(autoGenerate = false)
    var email: String="",
    @ColumnInfo("user-password")
    var password: String="",
    @ColumnInfo("user-photo")
    var profPic: ByteArray
)