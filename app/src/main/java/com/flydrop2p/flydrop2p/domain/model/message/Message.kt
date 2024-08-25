package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageType

sealed class Message : Comparable<Message> {
    abstract val messageId: Long
    abstract val senderId: Long
    abstract val receiverId: Long
    abstract val timestamp: Long
    abstract val isRead: Boolean

    abstract fun toMessageEntity(): MessageEntity

    override fun compareTo(other: Message): Int {
        return compareValuesBy(this, other, Message::timestamp)
    }
}

fun MessageEntity.toMessage(): Message {
    return when(messageType) {
        MessageType.TEXT_MESSAGE -> {
            this.toTextMessage()
        }

        MessageType.FILE_MESSAGE -> {
            this.toFileMessage()
        }
    }
}