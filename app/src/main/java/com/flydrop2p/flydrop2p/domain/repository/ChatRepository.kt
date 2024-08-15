package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.Message
import kotlinx.coroutines.flow.Flow
import java.util.TreeSet

interface ChatRepository {
    fun getChatMessagesByAccountId(accountId: Int): Flow<List<Message>>

    fun getAllChatPreviews(): Flow<List<ChatPreview>>

    suspend fun addChatMessage(message: Message)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}