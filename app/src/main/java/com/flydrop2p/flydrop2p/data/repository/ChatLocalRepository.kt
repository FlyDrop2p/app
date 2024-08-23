package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.local.account.AccountDAO
import com.flydrop2p.flydrop2p.data.local.message.MessageDAO
import com.flydrop2p.flydrop2p.data.local.profile.ProfileDAO
import com.flydrop2p.flydrop2p.domain.model.chat.ChatPreview
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.toAccount
import com.flydrop2p.flydrop2p.domain.model.contact.toProfile
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.toMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ChatLocalRepository(private val accountDAO: AccountDAO, private val messageDAO: MessageDAO, private val profileDAO: ProfileDAO) : ChatRepository {
    override fun getChatMessagesByAccountId(accountId: Int): Flow<List<Message>> {
        return messageDAO.getAllMessagesByAccountId(accountId).map { messageEntities ->
            messageEntities.map { it.toMessage() }
        }
    }

    override fun getAllChatPreviews(): Flow<List<ChatPreview>> {
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
                    messageDAO.getLastMessageByAccountId(account.accountId)?.toMessage()
                )
            }.sorted().reversed()
        }
    }

    override suspend fun addChatMessage(message: Message) {
        messageDAO.insertMessage(message.toMessageEntity())
    }
}