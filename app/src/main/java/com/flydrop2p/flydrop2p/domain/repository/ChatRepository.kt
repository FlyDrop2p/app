package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.message.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessagesByAccountId(accountId: Long): Flow<List<Message>>

    fun getAllChatPreviews(): Flow<List<ChatPreview>>

    suspend fun addChatMessage(message: Message): Long
}