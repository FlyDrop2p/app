package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageType

data class FileMessage(
    override val messageId: Long,
    override val senderId: Long,
    override val receiverId: Long,
    override val timestamp: Long,
    val fileName: String
) : Message() {
    override fun toMessageEntity(): MessageEntity {
        return MessageEntity(
            senderId = senderId,
            receiverId = receiverId,
            messageType = MessageType.FILE_MESSAGE,
            timestamp = timestamp,
            content = fileName
        )
    }
}

fun MessageEntity.toFileMessage(): FileMessage {
    return FileMessage(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        timestamp = timestamp,
        fileName = content
    )
}