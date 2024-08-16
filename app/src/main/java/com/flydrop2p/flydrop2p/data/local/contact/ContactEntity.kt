package com.flydrop2p.flydrop2p.data.local.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity(
    @PrimaryKey
    val accountId: Int,
    val username: String,
    val imagePath: String?
)
