package com.example.flydrop2p.domain.repository

import com.example.flydrop2p.domain.model.Chat
import com.example.flydrop2p.domain.model.Message

interface ChatRepository {

    suspend fun getChat(chatId: Int): Chat
    suspend fun getChatMessages(chatId: Int): List<Message>

    suspend fun addChatMessage(chatId: Int, message: Message)
}