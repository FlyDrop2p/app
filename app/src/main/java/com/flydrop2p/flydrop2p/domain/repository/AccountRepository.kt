package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val account: Flow<Account>

    suspend fun setAccountId(accountId: Int)
}