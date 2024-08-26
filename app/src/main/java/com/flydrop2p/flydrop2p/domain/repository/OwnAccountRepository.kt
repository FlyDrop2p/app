package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import kotlinx.coroutines.flow.Flow

interface OwnAccountRepository {
    fun getAccountAsFlow(): Flow<Account>
    suspend fun getAccount(): Account
    suspend fun setAccountId(accountId: Long)
    suspend fun setProfileUpdateTimestamp(profileUpdateTimestamp: Long)
}