package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllChatPreviewsAsFlow(): Flow<List<ChatPreview>>

    fun getAllMessagesByAccountIdAsFlow(accountId: Long): Flow<List<Message>>

    fun getAllMediaMessagesByAccountIdAsFlow(accountId: Long): Flow<List<Message>>

    fun getAllMessagesByReceiverAccountId(accountId: Long): List<Message>

    suspend fun getAllMessages(): List<Message>

    suspend fun getMessageByMessageId(messageId: Long): Message?

    suspend fun addMessage(message: Message): Long

    suspend fun addMessageWithId(message: Message): Long

    suspend fun updateMessage(message: Message)
    
    suspend fun updateMessageState(messageId: Long, messageState: MessageState)
}