package com.example.flydrop2p.domain.model

import com.example.flydrop2p.data.local.MessageEntity

data class Message(
    val messageId: Int,
    val chatId: Int,
    val message: String,
    val timestamp: String,
    val senderId: Int,
)

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        chatId = chatId,
        message = message,
        timestamp = timestamp,
        senderId = senderId,
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        messageId = messageId,
        chatId = chatId,
        message = message,
        timestamp = timestamp,
        senderId = senderId,
    )
}