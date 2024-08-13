package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoDAO
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.model.toChatInfoEntity
import com.flydrop2p.flydrop2p.domain.model.toChatInfo
import com.flydrop2p.flydrop2p.domain.repository.ChatsInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LocalChatInfoRepository(private val chatInfoDAO: ChatInfoDAO) : ChatsInfoRepository {

    override fun getChatsInfo(): Flow<List<ChatInfoEntity>> {
        return chatInfoDAO.getChatsInfo()
    }

    override fun getChatInfo(chatId: Long): Flow<ChatInfoEntity> {
        return chatInfoDAO.getChatInfoById(chatId)
    }

    override suspend fun addChatInfo(chatInfo: ChatInfoEntity) {
        chatInfoDAO.insertChatInfo(chatInfo)
    }

    override suspend fun updateChatInfo(chatInfo: ChatInfoEntity) {
        chatInfoDAO.updateChatInfo(chatInfo)
    }

    override suspend fun getChatInfoForDevice(deviceId: Long): Flow<ChatInfo?> {
        return flow {
            val chatInfoEntity = chatInfoDAO.getChatInfoByDeviceId(deviceId)
            emit(chatInfoEntity?.toChatInfo())
        }.flowOn(Dispatchers.IO)
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
