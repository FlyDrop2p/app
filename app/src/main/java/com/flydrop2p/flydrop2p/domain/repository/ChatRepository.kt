package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.network.Device
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessagesByChatId(chatId: Int): Flow<List<Message>>

    suspend fun getSingleChatIdByContactId(contactId: Int): Int?

    suspend fun addSingleChat(device: Device)

    suspend fun updateSingleChat(contact: Contact)

    suspend fun addChatMessage(message: Message)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}