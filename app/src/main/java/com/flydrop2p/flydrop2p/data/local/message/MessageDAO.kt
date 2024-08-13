package com.flydrop2p.flydrop2p.data.local.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Query("SELECT * FROM MessageEntity WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesByChatId(chatId: Int): Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity)
}