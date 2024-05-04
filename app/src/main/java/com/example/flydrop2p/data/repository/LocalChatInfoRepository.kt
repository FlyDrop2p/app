package com.example.flydrop2p.data.repository

import com.example.flydrop2p.data.DataSource
import com.example.flydrop2p.data.local.ChatInfoDAO
import com.example.flydrop2p.data.local.ChatInfoEntity
import com.example.flydrop2p.data.local.MessageDAO
import com.example.flydrop2p.domain.model.toChatInfoEntity
import com.example.flydrop2p.domain.repository.ChatsInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalChatInfoRepository (private val chatInfoDAO: ChatInfoDAO): ChatsInfoRepository {

    override fun getChatsInfo(): Flow<List<ChatInfoEntity>> {
        return chatInfoDAO.getChatsInfo()
    }

    override fun getChatInfo(chatId: Int): Flow<ChatInfoEntity> {
        return chatInfoDAO.getChatInfoById(chatId)
    }

    override suspend fun addChatInfo(chatInfo: ChatInfoEntity) {
        chatInfoDAO.insertChatInfo(chatInfo)
    }

    override suspend fun updateChatInfo(chatInfo: ChatInfoEntity) {
        chatInfoDAO.updateChatInfo(chatInfo)
    }

    // TODO: Only for testing purposes
    override suspend fun populateDatabase() {
        withContext(Dispatchers.IO) {
            val chatInfoList = DataSource.chatsInfoListDatasource
            for (chatInfo in chatInfoList) {
                chatInfoDAO.insertChatInfo(chatInfo.toChatInfoEntity())
            }
        }
    }



}