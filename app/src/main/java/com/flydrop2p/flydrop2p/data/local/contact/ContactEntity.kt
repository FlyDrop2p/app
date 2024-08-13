package com.flydrop2p.flydrop2p.data.local.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val accountId: Int = 0,
    val username: String,
    val imageFilePath: String?
)
