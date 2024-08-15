package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.network.model.NetworkTextMessage

data class TextMessage(
    val senderId: Int,
    val receiverId: Int,
    val text: String,
    val timestamp: Long
) : Comparable<TextMessage> {
    override fun compareTo(other: TextMessage): Int {
        return compareValuesBy(this, other, TextMessage::timestamp)
    }
}

fun TextMessage.toMessageEntity(): MessageEntity {
    return MessageEntity(
        senderId = senderId,
        receiverId = receiverId,
        content = text,
        timestamp = timestamp
    )
}

fun MessageEntity.toMessage(): TextMessage {
    return TextMessage(
        senderId = senderId,
        receiverId = receiverId,
        text = content,
        timestamp = timestamp
    )
}

fun TextMessage.toNetworkTextMessage(): NetworkTextMessage {
    return NetworkTextMessage(
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        timestamp = timestamp
    )
}

fun NetworkTextMessage.toTextMessage(): TextMessage {
    return TextMessage(
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        timestamp = timestamp
    )
}