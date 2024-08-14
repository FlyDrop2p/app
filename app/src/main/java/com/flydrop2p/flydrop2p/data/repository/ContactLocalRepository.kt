package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsDAO
import com.flydrop2p.flydrop2p.data.local.contact.ContactDAO
import com.flydrop2p.flydrop2p.domain.model.Account
import com.flydrop2p.flydrop2p.domain.model.toAccount
import com.flydrop2p.flydrop2p.domain.model.toContactEntity
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContactLocalRepository(private val contactDAO: ContactDAO, private val chatContactsDAO: ChatContactsDAO) : ContactRepository {

    override fun getAllContacts(): Flow<List<Account>> {
        return contactDAO.getAllContacts().map { contactEntities ->
            contactEntities.map { it.toAccount() }
        }
    }

    override suspend fun getContactById(accountId: Int): Account? {
        return contactDAO.getContactById(accountId)?.toAccount()
    }

    override suspend fun getChatContactsByChatId(chatId: Int): List<Account> {
        val contactIds = chatContactsDAO.getAllContactIdsByChatId(chatId)
        val accounts = mutableListOf<Account>()

        for (id in contactIds) {
            getContactById(id)?.also {
                accounts.add(it)
            }
        }

        return accounts
    }

    override suspend fun addContact(account: Account) {
        contactDAO.insertContact(account.toContactEntity())
    }

    override suspend fun updateContact(account: Account) {
        contactDAO.updateContact(account.toContactEntity())
    }

    override suspend fun deleteContact(account: Account) {
        contactDAO.deleteContact(account.toContactEntity())
    }

    // TODO: Only for testing purposes
    override suspend fun populateDatabase() {
        withContext(Dispatchers.IO) {
            val contacts = DataSource.contacts
            for (contact in contacts) {
                contactDAO.insertContact(contact.toContactEntity())
            }
        }
    }

}