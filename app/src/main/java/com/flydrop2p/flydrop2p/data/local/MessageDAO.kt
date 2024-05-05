package com.flydrop2p.flydrop2p.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM MessageEntity WHERE chatId = :chatId ORDER BY messageId ASC") // TODO: ORDER BY timestamp
    fun getMessagesByChatId(chatId: Int): Flow<List<MessageEntity>>

}