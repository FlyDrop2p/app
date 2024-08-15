package com.flydrop2p.flydrop2p.data.local.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Query("SELECT * FROM MessageEntity WHERE (senderId = :accountId OR receiverId = :accountId) ORDER BY timestamp ASC")
    fun getAllMessagesByAccountId(accountId: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM MessageEntity WHERE (senderId = :accountId OR receiverId = :accountId) ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessageByAccountId(accountId: Int): MessageEntity?

    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity)
}