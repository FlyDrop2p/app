package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Account>>

    suspend fun getContactById(accountId: Int): Account?

    suspend fun getChatContactsByChatId(chatId: Int): List<Account>

    suspend fun addContact(account: Account)

    suspend fun updateContact(account: Account)

    suspend fun deleteContact(account: Account)

    // TODO: Only for testing purposes
    suspend fun populateDatabase()
}