package com.example.flydrop2p.domain.repository

import com.example.flydrop2p.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getChatMessages(chatId: Int): Flow<List<MessageEntity>>
    suspend fun addChatMessage(messageEntity: MessageEntity)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}