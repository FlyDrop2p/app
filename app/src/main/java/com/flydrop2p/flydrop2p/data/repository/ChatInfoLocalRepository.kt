package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsDAO
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoDAO
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.model.toChatInfo
import com.flydrop2p.flydrop2p.domain.model.toChatInfoEntity
import com.flydrop2p.flydrop2p.domain.repository.ChatInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatInfoLocalRepository(private val chatInfoDAO: ChatInfoDAO, private val chatContactsDAO: ChatContactsDAO) : ChatInfoRepository {

    override fun getAllChatInfos(): Flow<List<ChatInfo>> {
        return chatInfoDAO.getAllChatInfos().map { chatInfoEntities ->
            chatInfoEntities.map { it.toChatInfo() }
        }
    }

    override suspend fun getChatInfoById(chatId: Int): ChatInfo? {
        return chatInfoDAO.getChatInfoById(chatId)?.toChatInfo()
    }

    override suspend fun getChatInfosByContactId(contactId: Int): List<ChatInfo> {
        val chatInfoIds = chatContactsDAO.getAllChatIdsByContactId(contactId)
        val chatInfos = mutableListOf<ChatInfo>()

        for (id in chatInfoIds) {
            getChatInfoById(id)?.also {
                chatInfos.add(it)
            }
        }

        return chatInfos
    }

    override suspend fun addChatInfo(chatInfo: ChatInfo) {
        chatInfoDAO.insertChatInfo(chatInfo.toChatInfoEntity())
    }

    override suspend fun updateChatInfo(chatInfo: ChatInfo) {
        chatInfoDAO.updateChatInfo(chatInfo.toChatInfoEntity())
    }

    override suspend fun deleteChatInfo(chatInfo: ChatInfo) {
        chatInfoDAO.deleteChatInfo(chatInfo.toChatInfoEntity())
    }

    override suspend fun populateDatabase() {
        withContext(Dispatchers.IO) {
            val chatInfoList = DataSource.chatsInfoListDatasource
            for (chatInfo in chatInfoList) {
                chatInfoDAO.insertChatInfo(chatInfo.toChatInfoEntity())
            }
            val chatContacts = DataSource.chatContacts
            for (contact in chatContacts) {
                chatContactsDAO.insertChatContacts(contact)
            }
        }
    }
}
