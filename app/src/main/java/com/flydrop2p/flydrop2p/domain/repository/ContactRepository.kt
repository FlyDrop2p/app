package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Contact>>

    suspend fun getContactById(contactId: Int): Contact?

    suspend fun getChatContactsByChatId(chatId: Int): List<Contact>

    suspend fun addContact(contact: Contact)

    suspend fun updateContact(contact: Contact)

    suspend fun deleteContact(contact: Contact)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}