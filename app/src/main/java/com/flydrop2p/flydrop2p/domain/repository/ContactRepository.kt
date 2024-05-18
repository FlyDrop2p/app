package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.data.local.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContacts(): Flow<List<ContactEntity>>

    fun getContact(contactId: Int): Flow<ContactEntity?>

    suspend fun addContact(contact: ContactEntity)

    suspend fun updateContact(contact: ContactEntity)

    suspend fun deleteContact(contact: ContactEntity)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}