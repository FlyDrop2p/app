package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageType
import com.flydrop2p.flydrop2p.network.model.message.NetworkTextMessage

data class TextMessage(
    override var messageId: Long,
    override val senderId: Long,
    override val receiverId: Long,
    override val timestamp: Long,
    val text: String
) : Message() {
    override fun toMessageEntity(): MessageEntity {
        return MessageEntity(
            senderId = senderId,
            receiverId = receiverId,
            messageType = MessageType.TEXT_MESSAGE,
            timestamp = timestamp,
            content = text
        )
    }
}

fun MessageEntity.toTextMessage(): TextMessage {
    return TextMessage(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        timestamp = timestamp,
        text = content
    )
}

fun TextMessage.toNetworkTextMessage(): NetworkTextMessage {
    return NetworkTextMessage(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        timestamp = timestamp,
        text = text
    )
}

fun NetworkTextMessage.toTextMessage(): TextMessage {
    return TextMessage(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        timestamp = timestamp,
        text = text
    )
}