package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.network.model.NetworkTextMessage

data class Message(
    val messageId: Int = 0,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val timestamp: Long
) : Comparable<Message> {
    constructor(networkTextMessage: NetworkTextMessage, receiverId: Int) : this(
        senderId = networkTextMessage.senderId,
        receiverId = receiverId,
        content = networkTextMessage.content,
        timestamp = networkTextMessage.timestamp
    )

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