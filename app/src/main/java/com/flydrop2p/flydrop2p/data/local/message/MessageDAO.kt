package com.flydrop2p.flydrop2p.data.local.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Query("SELECT * FROM MessageEntity WHERE (senderId = :accountId OR receiverId = :accountId) ORDER BY timestamp ASC")
    fun getAllMessagesByAccountId(accountId: Long): Flow<List<MessageEntity>>

    @Query("SELECT COUNT(*) FROM MessageEntity WHERE senderId = :accountId AND messageState = 'MESSAGE_RECEIVED'")
    suspend fun getNumberOfUnreadMessagesByAccountId(accountId: Long): Long

    @Query("SELECT * FROM MessageEntity WHERE (senderId = :accountId OR receiverId = :accountId) ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessageByAccountId(accountId: Long): MessageEntity?

    @Query("SELECT * FROM MessageEntity WHERE messageId = :messageId")
    suspend fun getMessageByMessageId(messageId: Long): MessageEntity?

    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity): Long

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)

    @Query("UPDATE MessageEntity SET messageState = :messageState WHERE messageId = :messageId")
    suspend fun updateMessageState(messageId: Long, messageState: MessageState)
}