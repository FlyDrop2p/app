package com.flydrop2p.flydrop2p.data.local.message

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageType {
    TEXT_MESSAGE, FILE_MESSAGE
}

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val messageId: Int = 0,
    val senderId: Int,
    val receiverId: Int,
    val messageType: MessageType,
    val content: String,
    val timestamp: Long
)