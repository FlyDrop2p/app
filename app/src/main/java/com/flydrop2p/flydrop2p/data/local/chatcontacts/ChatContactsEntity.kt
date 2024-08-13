package com.flydrop2p.flydrop2p.data.local.chatcontacts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatContactsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chatId: Int,
    val contactId: Int
)