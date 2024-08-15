package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity

data class Message(
    val messageId: Int,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val timestamp: Long
) : Comparable<Message> {
    override fun compareTo(other: Message): Int {
        return compareValuesBy(this, other, Message::timestamp)
    }
}

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        timestamp = timestamp
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        timestamp = timestamp
    )
}