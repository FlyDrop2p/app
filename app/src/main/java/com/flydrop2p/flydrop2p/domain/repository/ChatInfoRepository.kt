package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatInfoRepository {
    fun getAllChatInfos(): Flow<List<ChatInfo>>

    suspend fun getChatInfoById(chatId: Int): ChatInfo?

    suspend fun getChatInfosByContactId(contactId: Int): List<ChatInfo>

    suspend fun addChatInfo(chatInfo: ChatInfo)

    suspend fun updateChatInfo(chatInfo: ChatInfo)

    suspend fun deleteChatInfo(chatInfo: ChatInfo)


    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}