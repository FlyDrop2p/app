package com.flydrop2p.flydrop2p.data.local.message

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageType {
    TEXT_MESSAGE, FILE_MESSAGE
}

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long = 0,
    val senderId: Long,
    val receiverId: Long,
    val timestamp: Long,
    val messageType: MessageType,
    val content: String,
    val isRead: Boolean
)