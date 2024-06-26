package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.data.local.ChatInfoEntity
import kotlinx.coroutines.flow.Flow

interface ChatsInfoRepository {
    fun getChatsInfo(): Flow<List<ChatInfoEntity>>

    fun getChatInfo(chatId: Int): Flow<ChatInfoEntity>

    suspend fun addChatInfo(chatInfo: ChatInfoEntity)

    suspend fun updateChatInfo(chatInfo: ChatInfoEntity)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}