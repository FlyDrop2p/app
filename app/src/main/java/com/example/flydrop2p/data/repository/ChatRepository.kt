package com.example.flydrop2p.data.repository

import com.example.flydrop2p.data.DataSource
import com.example.flydrop2p.domain.model.Chat
import com.example.flydrop2p.domain.model.Message
import com.example.flydrop2p.domain.repository.ChatRepository

class ChatRepositoryImpl : ChatRepository {

    override suspend fun getChat(chatId: Int): Chat {
        return DataSource.getChat(chatId)
    }
    override suspend fun getChatMessages(chatId: Int): List<Message> {
        return emptyList()
    }

    override suspend fun addChatMessage(chatId: Int, message: Message) {
        // Add message to chat
    }
}