package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessagesByChatId(chatId: Int): Flow<List<Message>>

    suspend fun addChatMessage(message: Message)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}