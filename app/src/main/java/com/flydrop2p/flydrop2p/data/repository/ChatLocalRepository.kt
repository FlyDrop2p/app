package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.domain.model.Chat
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.domain.model.toMessage
import com.flydrop2p.flydrop2p.domain.model.toMessageEntity
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatLocalRepository(private val messageDAO: MessageDAO) : ChatRepository {

    override fun getChatMessagesByChatId(chatId: Int): Flow<List<Message>> {
        return messageDAO.getMessagesByChatId(chatId).map { messageEntities ->
            messageEntities.map { it.toMessage() }
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
                    messageDAO.insertMessage(message)
                }
            }
        }
    }
}