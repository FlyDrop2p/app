package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage

data class FileMessage(
    override val messageId: Long,
    override val senderId: Long,
    override val receiverId: Long,
    override val timestamp: Long,
    override val messageState: MessageState,
    val fileName: String
) : Message() {
    constructor(networkFileMessage: NetworkFileMessage, messageState: MessageState, fileName: String) : this(
        messageId = networkFileMessage.messageId,
        senderId = networkFileMessage.senderId,
        receiverId = networkFileMessage.receiverId,
        timestamp = networkFileMessage.timestamp,
        messageState = messageState,
        fileName = fileName
    )

    override fun toMessageEntity(): MessageEntity {
        return MessageEntity(
            senderId = senderId,
            receiverId = receiverId,
            messageType = MessageType.FILE_MESSAGE,
            timestamp = timestamp,
            messageState = messageState,
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
        messageState = messageState,
        fileName = content
    )
}