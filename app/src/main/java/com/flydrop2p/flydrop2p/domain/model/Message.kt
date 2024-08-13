package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity

data class Message(
    val messageId: Int,
    val chatId: Int,
    val senderId: Int,
    val content: String,
    val timestamp: Long
)

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp
    )
}