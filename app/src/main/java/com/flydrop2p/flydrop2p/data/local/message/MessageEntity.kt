package com.flydrop2p.flydrop2p.data.local.message

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val messageId: Int = 0,
    val chatId: Int,
    val senderId: Int,
    val content: String,
    val timestamp: Long
)
