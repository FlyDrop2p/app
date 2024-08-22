package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.message.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessagesByAccountId(accountId: Int): Flow<List<Message>>

    fun getAllChatPreviews(): Flow<List<ChatPreview>>

    suspend fun addChatMessage(message: Message)
}