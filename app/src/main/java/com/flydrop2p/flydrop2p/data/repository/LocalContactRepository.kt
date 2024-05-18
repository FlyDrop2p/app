package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.DataSource
import com.flydrop2p.flydrop2p.data.local.ContactDAO
import com.flydrop2p.flydrop2p.data.local.ContactEntity
import com.flydrop2p.flydrop2p.domain.model.toContactEntity
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalContactRepository (private val contactDAO: ContactDAO): ContactRepository {

    override fun getContacts(): Flow<List<ContactEntity>> {
        return contactDAO.getContacts()
    }

    override fun getContact(contactId: Int): Flow<ContactEntity?> {
        return contactDAO.getContactById(contactId)
    }

    override suspend fun addContact(contact: ContactEntity) {
        contactDAO.insertContact(contact)
    }

    override suspend fun updateContact(contact: ContactEntity) {
        contactDAO.updateContact(contact)
    }

    override suspend fun deleteContact(contact: ContactEntity) {
        contactDAO.deleteContact(contact.id)
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