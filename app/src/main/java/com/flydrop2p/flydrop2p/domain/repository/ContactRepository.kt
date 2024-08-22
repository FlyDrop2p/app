package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Contact>>

    fun getContactByAccountId(accountId: Int): Flow<Contact?>

    suspend fun addAccount(account: Account)

    suspend fun addOrUpdateAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun addProfile(profile: Profile)

    suspend fun addOrUpdateProfile(profile: Profile)

    suspend fun updateProfile(profile: Profile)

    suspend fun deleteProfile(profile: Profile)
}
