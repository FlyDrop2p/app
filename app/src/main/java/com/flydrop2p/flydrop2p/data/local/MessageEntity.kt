package com.flydrop2p.flydrop2p.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val messageId: Int = 0,
    val chatId: Int,
    val message: String,
    val timestamp: String,
    val senderId: Int,
)
