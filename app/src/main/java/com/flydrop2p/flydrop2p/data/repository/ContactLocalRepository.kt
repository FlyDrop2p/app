package com.flydrop2p.flydrop2p.data.repository

import com.flydrop2p.flydrop2p.data.local.account.AccountDAO
import com.flydrop2p.flydrop2p.data.local.profile.ProfileDAO
import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toAccount
import com.flydrop2p.flydrop2p.domain.model.contact.toAccountEntity
import com.flydrop2p.flydrop2p.domain.model.contact.toProfile
import com.flydrop2p.flydrop2p.domain.model.contact.toProfileEntity
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ContactLocalRepository(private val accountDAO: AccountDAO, private val profileDAO: ProfileDAO) : ContactRepository {
    override fun getAllContacts(): Flow<List<Contact>> {
        val accountsFlow = accountDAO.getAllAccounts().map { accountEntities ->
            accountEntities.map { it.toAccount() }
        }

        val profilesFlow = profileDAO.getAllProfiles().map { profileEntities ->
            profileEntities.map { it.toProfile() }
        }

        return combine(accountsFlow, profilesFlow) { accounts, profiles ->
            accounts.map { account ->
                Contact(account, profiles.find { it.accountId == account.accountId })
            }
        }
    }

    override fun getContactByAccountId(accountId: Int): Flow<Contact?> {
        val accountFlow = accountDAO.getAccountByAccountId(accountId).map { accountEntity ->
            accountEntity?.toAccount()
        }

        val profileFlow = profileDAO.getProfileByAccountId(accountId).map { profileEntity ->
            profileEntity?.toProfile()
        }

        return combine(accountFlow, profileFlow) { account, profile ->
            if(account != null && profile != null) {
                Contact(account, profile)
            } else {
                null
            }
        }
    }

    override suspend fun addAccount(account: Account) {
        accountDAO.insertAccount(account.toAccountEntity())
    }

    override suspend fun addOrUpdateAccount(account: Account) {
        accountDAO.insertOrUpdateAccount(account.toAccountEntity())
    }

    override suspend fun updateAccount(account: Account) {
        accountDAO.updateAccount(account.toAccountEntity())
    }

    override suspend fun deleteAccount(account: Account) {
        accountDAO.deleteAccount(account.toAccountEntity())
    }

    override suspend fun addProfile(profile: Profile) {
        profileDAO.insertProfile(profile.toProfileEntity())
    }

    override suspend fun addOrUpdateProfile(profile: Profile) {
        profileDAO.insertOrUpdateProfile(profile.toProfileEntity())
    }

    override suspend fun updateProfile(profile: Profile) {
        profileDAO.updateProfile(profile.toProfileEntity())
    }

    override suspend fun deleteProfile(profile: Profile) {
        profileDAO.deleteProfile(profile.toProfileEntity())
    }

}