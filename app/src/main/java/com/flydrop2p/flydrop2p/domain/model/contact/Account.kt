package com.flydrop2p.flydrop2p.domain.model.contact

import com.flydrop2p.flydrop2p.data.local.account.AccountEntity
import com.flydrop2p.flydrop2p.network.model.contact.NetworkAccount

data class Account(
    val accountId: Long,
    val profileUpdateTimestamp: Long
)

fun Account.toAccountEntity(): AccountEntity {
    return AccountEntity(
        accountId = accountId,
        profileUpdateTimestamp = profileUpdateTimestamp
    )
}

fun AccountEntity.toAccount(): Account {
    return Account(
        accountId = accountId,
        profileUpdateTimestamp = profileUpdateTimestamp
    )
}

fun Account.toNetworkAccount(): NetworkAccount {
    return NetworkAccount(
        accountId = accountId,
        profileUpdateTimestamp = profileUpdateTimestamp
    )
}

fun NetworkAccount.toAccount(): Account {
    return Account(
        accountId = accountId,
        profileUpdateTimestamp = profileUpdateTimestamp
    )
}