package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.TextMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessagesByAccountId(accountId: Int): Flow<List<TextMessage>>

    fun getAllChatPreviews(): Flow<List<ChatPreview>>

    suspend fun addChatMessage(message: TextMessage)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}