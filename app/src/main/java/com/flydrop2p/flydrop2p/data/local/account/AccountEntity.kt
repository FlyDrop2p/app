package com.flydrop2p.flydrop2p.data.local.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(
    @PrimaryKey
    val accountId: Int,
    val profileUpdate: Long
)