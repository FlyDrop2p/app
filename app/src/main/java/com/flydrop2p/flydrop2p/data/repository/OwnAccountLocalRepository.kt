package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.data.local.account.AccountEntity
import com.flydrop2p.flydrop2p.domain.model.device.Account
import com.flydrop2p.flydrop2p.domain.model.device.toAccount
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class OwnAccountLocalRepository(private val ownAccountDataStore: DataStore<AccountEntity>) : OwnAccountRepository {
    override fun getAccountAsFlow(): Flow<Account> {
        return ownAccountDataStore.data.map { it.toAccount() }
    }

    override suspend fun getAccount(): Account {
        return ownAccountDataStore.data.first().toAccount()
    }

    override suspend fun setAccountId(accountId: Long) {
        ownAccountDataStore.updateData { it.copy(accountId = accountId) }
    }

    override suspend fun setProfileUpdateTimestamp(profileUpdateTimestamp: Long) {
        ownAccountDataStore.updateData { it.copy(profileUpdateTimestamp = profileUpdateTimestamp) }
    }
}