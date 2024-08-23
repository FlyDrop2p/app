package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContactsAsFlow(): Flow<List<Contact>>
    fun getContactByAccountIdAsFlow(accountId: Int): Flow<Contact?>

    fun getAllAccountsAsFlow(): Flow<List<Account>>
    fun getAccountByAccountIdAsFlow(accountId: Int): Flow<Account?>
    suspend fun getAllAccounts(): List<Account>
    suspend fun getAccountByAccountId(accountId: Int): Account?
    suspend fun addAccount(account: Account)
    suspend fun addOrUpdateAccount(account: Account)
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(account: Account)

    fun getAllProfilesAsFlow(): Flow<List<Profile>>
    fun getProfileByAccountIdAsFlow(accountId: Int): Flow<Profile?>
    suspend fun getAllProfiles(): List<Profile>
    suspend fun getProfileByAccountId(accountId: Int): Profile?
    suspend fun addProfile(profile: Profile)
    suspend fun addOrUpdateProfile(profile: Profile)
    suspend fun updateProfile(profile: Profile)
    suspend fun deleteProfile(profile: Profile)
}
