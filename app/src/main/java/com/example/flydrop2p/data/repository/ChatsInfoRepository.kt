package com.example.flydrop2p.data.repository

import com.example.flydrop2p.data.DataSource
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.repository.ChatsInfoRepository

class ChatsInfoRepositoryImpl : ChatsInfoRepository {
    override suspend fun getChatsInfo(): List<ChatInfo> {
        return DataSource.getChatsInfoList()
    }

    override suspend fun getChatInfo(chatId: Int): ChatInfo {
        return ChatInfo(0, "", "", "")
    }

    override suspend fun addChatInfo(chatInfo: ChatInfo) {
    }

    override suspend fun updateChatInfo(chatInfo: ChatInfo) {
    }
}