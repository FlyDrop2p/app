package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getChatMessages(chatId: Int): Flow<List<MessageEntity>>
    suspend fun addChatMessage(messageEntity: MessageEntity)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}