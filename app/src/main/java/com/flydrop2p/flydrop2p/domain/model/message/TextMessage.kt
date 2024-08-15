package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageType
import com.flydrop2p.flydrop2p.network.model.NetworkTextMessage

data class TextMessage(
    override val senderId: Int,
    override val receiverId: Int,
    val text: String,
    override val timestamp: Long
) : Message() {
    override fun toMessageEntity(): MessageEntity {
        return MessageEntity(
            senderId = senderId,
            receiverId = receiverId,
            messageType = MessageType.TEXT_MESSAGE,
            content = text,
            timestamp = timestamp
        )    }
}

fun MessageEntity.toTextMessage(): TextMessage {
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