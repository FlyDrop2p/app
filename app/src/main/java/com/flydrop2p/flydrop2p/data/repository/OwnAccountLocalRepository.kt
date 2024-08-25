package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class OwnAccountLocalRepository(private val ownAccountDataStore: DataStore<Account>) : OwnAccountRepository {
    override fun getAccountAsFlow(): Flow<Account> {
        return ownAccountDataStore.data
    }

    override suspend fun getAccount(): Account {
        return ownAccountDataStore.data.first()
    }

    override suspend fun setAccountId(accountId: Long) {
        ownAccountDataStore.updateData { it.copy(accountId = accountId) }
    }

    override suspend fun setProfileUpdate(profileUpdate: Long) {
        ownAccountDataStore.updateData { it.copy(profileUpdate = profileUpdate) }
    }
}