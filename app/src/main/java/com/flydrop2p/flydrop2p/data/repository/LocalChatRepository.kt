package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalChatRepository(private val messageDAO: MessageDAO) : ChatRepository {

    override suspend fun getChatMessages(chatId: Int): Flow<List<MessageEntity>> {
        return messageDAO.getMessagesByChatId(chatId)
    }

    override suspend fun addChatMessage(messageEntity: MessageEntity) {
        messageDAO.insertMessage(messageEntity)
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