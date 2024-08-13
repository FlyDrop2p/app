package com.flydrop2p.flydrop2p.data.local.chatinfo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatInfoDAO {
    @Query("SELECT * FROM ChatInfoEntity")
    fun getAllChatInfos(): Flow<List<ChatInfoEntity>>

    @Query("SELECT * FROM ChatInfoEntity WHERE chatId = :chatId")
    suspend fun getChatInfoById(chatId: Int): ChatInfoEntity?

    @Insert
    suspend fun insertChatInfo(chatInfoEntity: ChatInfoEntity)

    @Update
    suspend fun updateChatInfo(chatInfoEntity: ChatInfoEntity)

    @Delete
    suspend fun deleteChatInfo(chatInfoEntity: ChatInfoEntity)
}