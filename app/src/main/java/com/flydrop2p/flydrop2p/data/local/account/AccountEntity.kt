package com.flydrop2p.flydrop2p.data.local.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class AccountEntity(
    @PrimaryKey
    val accountId: Long,
    val profileUpdate: Long
)
