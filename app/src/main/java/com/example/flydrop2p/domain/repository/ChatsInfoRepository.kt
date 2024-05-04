package com.example.flydrop2p.domain.repository

import com.example.flydrop2p.data.local.ChatInfoEntity
import com.example.flydrop2p.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatsInfoRepository {
    fun getChatsInfo(): Flow<List<ChatInfoEntity>>

    fun getChatInfo(chatId: Int): Flow<ChatInfoEntity>

    suspend fun addChatInfo(chatInfo: ChatInfoEntity)

    suspend fun updateChatInfo(chatInfo: ChatInfoEntity)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}