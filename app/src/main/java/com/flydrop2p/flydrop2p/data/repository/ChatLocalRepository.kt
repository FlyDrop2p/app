package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.local.account.AccountDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.contact.toContact
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.toMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatLocalRepository(private val accountDAO: AccountDAO, private val messageDAO: MessageDAO) : ChatRepository {
    override fun getChatMessagesByAccountId(accountId: Int): Flow<List<Message>> {
        return messageDAO.getAllMessagesByAccountId(accountId).map { messageEntities ->
            messageEntities.map { it.toMessage() }
        }
    }

    override fun getAllChatPreviews(): Flow<List<ChatPreview>> {
        return accountDAO.getAllAccounts().map { contactEntities ->
            contactEntities.map { ChatPreview(it.toContact(), messageDAO.getLastMessageByAccountId(it.accountId)?.toMessage()) }.sorted().reversed()
        }
    }

    override suspend fun addChatMessage(message: Message) {
        messageDAO.insertMessage(message.toMessageEntity())
    }
}