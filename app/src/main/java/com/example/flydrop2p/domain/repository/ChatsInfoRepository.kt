package com.example.flydrop2p.domain.repository

import com.example.flydrop2p.domain.model.ChatInfo

interface ChatsInfoRepository {
    suspend fun getChatsInfo(): List<ChatInfo>

    suspend fun getChatInfo(chatId: Int): ChatInfo

    suspend fun addChatInfo(chatInfo: ChatInfo)

    suspend fun updateChatInfo(chatInfo: ChatInfo)
}