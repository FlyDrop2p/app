package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.contact.ContactDAO
import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.toContact
import com.flydrop2p.flydrop2p.domain.model.toContactEntity
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContactLocalRepository(private val contactDAO: ContactDAO) : ContactRepository {

    override fun getAllContacts(): Flow<List<Contact>> {
        return contactDAO.getAllContacts().map { contactEntities ->
            contactEntities.map { it.toContact() }
        }
    }

    override fun getContactById(accountId: Int): Flow<Contact?> {
        return contactDAO.getContactById(accountId).map { it?.toContact() }
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