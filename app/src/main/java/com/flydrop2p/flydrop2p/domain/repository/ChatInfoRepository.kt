package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatsInfoRepository {
    fun getChatsInfo(): Flow<List<ChatInfo>>

    fun getChatInfo(chatId: Int): Flow<ChatInfo>

    suspend fun addChatInfo(chatInfo: ChatInfo)

    suspend fun updateChatInfo(chatInfo: ChatInfo)

    suspend fun getChatInfoForDevice(deviceId: Int): Flow<ChatInfo?>

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}