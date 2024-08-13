package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.chatcontacts.ChatContactsDAO
import com.flydrop2p.flydrop2p.data.local.contact.ContactDAO
import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.toContact
import com.flydrop2p.flydrop2p.domain.model.toContactEntity
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContactLocalRepository(private val contactDAO: ContactDAO, private val chatContactsDAO: ChatContactsDAO) : ContactRepository {

    override fun getAllContacts(): Flow<List<Contact>> {
        return contactDAO.getAllContacts().map { contactEntities ->
            contactEntities.map { it.toContact() }
        }
    }

    override suspend fun getContactById(contactId: Int): Contact? {
        return contactDAO.getContactById(contactId)?.toContact()
    }

    override suspend fun getChatContactsByChatId(chatId: Int): List<Contact> {
        val contactIds = chatContactsDAO.getAllContactIdsByChatId(chatId)
        val contacts = mutableListOf<Contact>()

        for (id in contactIds) {
            getContactById(id)?.also {
                contacts.add(it)
            }
        }

        return contacts
    }

    override suspend fun addContact(contact: Contact) {
        contactDAO.insertContact(contact.toContactEntity())
    }

    override suspend fun updateContact(contact: Contact) {
        contactDAO.updateContact(contact.toContactEntity())
    }

    override suspend fun deleteContact(contact: Contact) {
        contactDAO.deleteContact(contact.toContactEntity())
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