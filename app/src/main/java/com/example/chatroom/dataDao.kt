package com.example.chatroom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
abstract class dataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addUser(userEntity: dataToCache)

    @Query("Select * from `userdata-table`")
    abstract fun getUserInfo(): Flow<dataToCache>

    @Update
    abstract fun updateData(userEntity: dataToCache)

    @Delete
    abstract fun deleteData(userEntity: dataToCache)
}