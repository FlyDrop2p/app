package com.flydrop2p.flydrop2p.data.local.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileEntity(
    @PrimaryKey
    val accountId: Long,
    val username: String,
    val imageFileName: String?
)