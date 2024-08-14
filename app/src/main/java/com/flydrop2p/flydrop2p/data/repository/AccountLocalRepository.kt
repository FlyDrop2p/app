package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.domain.model.Account
import com.flydrop2p.flydrop2p.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class AccountLocalRepository(private val accountDataStore: DataStore<Account>) : AccountRepository {
    override val account: Flow<Account>
        get() = accountDataStore.data

    override suspend fun setAccountId(accountId: Int) {
        accountDataStore.updateData { it.copy(accountId = accountId) }
    }
}