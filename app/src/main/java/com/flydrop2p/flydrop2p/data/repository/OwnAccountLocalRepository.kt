package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import kotlinx.coroutines.flow.Flow

class OwnAccountLocalRepository(private val ownAccountDataStore: DataStore<Account>) : OwnAccountRepository {
    override val account: Flow<Account>
        get() = ownAccountDataStore.data

    override suspend fun setAccountId(accountId: Int) {
        ownAccountDataStore.updateData { it.copy(accountId = accountId) }
    }

    override suspend fun setProfileUpdate(profileUpdate: Long) {
        ownAccountDataStore.updateData { it.copy(profileUpdate = profileUpdate) }
    }
}