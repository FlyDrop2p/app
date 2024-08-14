package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsDAO
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsEntity
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoDAO
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatType
import com.flydrop2p.flydrop2p.data.local.contact.ContactDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.domain.model.toContactEntity
import com.flydrop2p.flydrop2p.domain.model.toMessage
import com.flydrop2p.flydrop2p.domain.model.toMessageEntity
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.toContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatLocalRepository(
    private val chatContactsDAO: ChatContactsDAO,
    private val chatInfoDAO: ChatInfoDAO,
    private val contactDAO: ContactDAO,
    private val messageDAO: MessageDAO
) : ChatRepository {

    override fun getChatMessagesByChatId(chatId: Int): Flow<List<Message>> {
        return messageDAO.getMessagesByChatId(chatId).map { messageEntities ->
            messageEntities.map { it.toMessage() }
        }
    }

    override suspend fun getSingleChatIdByContactId(contactId: Int): Int? {
        val chatIds = chatContactsDAO.getAllChatIdsByContactId(contactId)

        for(chatId in chatIds) {
            val chatInfo = chatInfoDAO.getChatInfoById(chatId)

            chatInfo?.let {
                if(chatInfo.chatType == ChatType.SINGLE) {
                    return chatInfo.chatId
                }
            }
        }

        return null
    }

    override suspend fun addSingleChat(device: Device) {
        contactDAO.insertContact(device.toContact().toContactEntity())
        val chatId = chatInfoDAO.insertChatInfo(ChatInfoEntity(chatType = ChatType.SINGLE, name = device.username))
        chatContactsDAO.insertChatContacts(ChatContactsEntity(chatId = chatId.toInt(), contactId = device.accountId))
    }

    override suspend fun updateSingleChat(contact: Contact) {
        contactDAO.updateContact(contact.toContactEntity())
        val chatId = getSingleChatIdByContactId(contact.accountId)

        chatId?.let {
            chatInfoDAO.updateChatInfo(ChatInfoEntity(chatId = it, chatType = ChatType.SINGLE, name = contact.username))
        }
    }

    override suspend fun addChatMessage(message: Message) {
        messageDAO.insertMessage(message.toMessageEntity())
    }


    // TODO: Only for testing purposes
    override suspend fun populateDatabase() {
        withContext(Dispatchers.IO) {
            for ((chatId, messages) in DataSource.placeholderMessages) {
                for (message in messages) {
                    messageDAO.insertMessage(message.toMessageEntity())
                }
            }
        }
    }
}