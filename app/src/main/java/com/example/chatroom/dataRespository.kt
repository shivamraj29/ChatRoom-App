package com.example.chatroom

import kotlinx.coroutines.flow.Flow

class dataRespository(val dataDao: dataDao) {

    suspend fun addUser(dataToCache: dataToCache){
        dataDao.addUser(dataToCache)
    }

    suspend fun getUser():Flow<dataToCache>{
        return dataDao.getUserInfo()
    }

    suspend fun updateData(dataToCache: dataToCache){
        dataDao.updateData(dataToCache)
    }

    suspend fun deleteData(dataToCache: dataToCache){
        dataDao.deleteData(dataToCache)
    }
}