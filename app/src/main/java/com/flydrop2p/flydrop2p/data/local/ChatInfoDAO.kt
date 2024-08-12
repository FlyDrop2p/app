package com.flydrop2p.flydrop2p.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatInfoDAO {
    @Query("SELECT * FROM ChatInfoEntity WHERE id = :chatId")
    fun getChatInfoById(chatId: Int): Flow<ChatInfoEntity>

    @Query("SELECT * FROM ChatInfoEntity")
    fun getChatsInfo(): Flow<List<ChatInfoEntity>>

    @Insert
    suspend fun insertChatInfo(chatInfoEntity: ChatInfoEntity)

    @Update
    suspend fun updateChatInfo(chatInfoEntity: ChatInfoEntity)

    @Delete
    suspend fun deleteChatInfo(chatInfoEntity: ChatInfoEntity)

    @Query("SELECT * FROM ChatInfoEntity WHERE id = :id")
    suspend fun getChatInfoByDeviceId(id: Long): ChatInfoEntity?
}