package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.local.account.AccountDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.data.local.profile.ProfileDAO
import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.device.Contact
import com.flydrop2p.flydrop2p.domain.model.device.toAccount
import com.flydrop2p.flydrop2p.domain.model.device.toProfile
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import com.flydrop2p.flydrop2p.domain.model.message.toMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ChatLocalRepository(private val accountDAO: AccountDAO, private val messageDAO: MessageDAO, private val profileDAO: ProfileDAO) : ChatRepository {
    override fun getAllChatPreviewsAsFlow(): Flow<List<ChatPreview>> {
        val accountsFlow = accountDAO.getAllAccountsAsFlow().map { accountEntities ->
            accountEntities.map { it.toAccount() }
        }

        val profilesFlow = profileDAO.getAllProfilesAsFlow().map { profileEntities ->
            profileEntities.map { it.toProfile() }
        }

        return combine(accountsFlow, profilesFlow) { accounts, profiles ->
            accounts.map { account ->
                ChatPreview(
                    Contact(account, profiles.find { it.accountId == account.accountId }),
                    messageDAO.getCountOfUnreadMessagesByAccountId(account.accountId).toInt(),
                    messageDAO.getLastMessageByAccountId(account.accountId)?.toMessage()
                )
            }.sorted().reversed()
        }
    }

    override fun getAllMessagesByAccountIdAsFlow(accountId: Long): Flow<List<Message>> {
        return messageDAO.getAllMessagesByAccountId(accountId).map { messageEntities ->
            messageEntities.map { it.toMessage() }
        }
    }

    override fun getAllMediaMessagesByAccountIdAsFlow(accountId: Long): Flow<List<Message>> {
        return messageDAO.getAllMediaMessagesByAccountId(accountId).map { messageEntities ->
            messageEntities.map { it.toMessage() }
        }
    }

    override fun getAllMessagesByReceiverAccountId(accountId: Long): List<Message> {
        return messageDAO.getMessagesByReceiverAccountId(accountId).map { it.toMessage() }
    }

    override suspend fun getAllMessages(): List<Message> {
        return messageDAO.getAllMessages().map { it.toMessage() }
    }

    override suspend fun getMessageByMessageId(messageId: Long): Message? {
        return messageDAO.getMessageByMessageId(messageId)?.toMessage()
    }

    override suspend fun addMessage(message: Message): Long {
        return messageDAO.insertMessage(message.toMessageEntity())
    }

    override suspend fun updateMessage(message: Message) {
        messageDAO.updateMessage(message.toMessageEntity())
    }

    override suspend fun updateMessageState(messageId: Long, messageState: MessageState) {
        messageDAO.updateMessageState(messageId, messageState)
    }
}