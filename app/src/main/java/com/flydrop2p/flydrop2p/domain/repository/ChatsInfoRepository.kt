package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatsInfoRepository {
    fun getChatsInfo(): Flow<List<ChatInfoEntity>>

    fun getChatInfo(chatId: Long): Flow<ChatInfoEntity>

    suspend fun addChatInfo(chatInfo: ChatInfoEntity)

    suspend fun updateChatInfo(chatInfo: ChatInfoEntity)

    suspend fun getChatInfoForDevice(deviceId: Long): Flow<ChatInfo?>

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}