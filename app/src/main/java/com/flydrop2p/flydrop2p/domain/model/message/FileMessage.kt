package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageType
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage
import java.io.File

data class FileMessage(
    override val senderId: Int,
    override val receiverId: Int,
    val file: File,
    override val timestamp: Long
) : Message() {
    override fun toMessageEntity(): MessageEntity {
        return MessageEntity(
            senderId = senderId,
            receiverId = receiverId,
            messageType = MessageType.FILE_MESSAGE,
            content = file.path,
            timestamp = timestamp
        )
    }
}

fun MessageEntity.toFileMessage(): FileMessage {
    return FileMessage(
        senderId = senderId,
        receiverId = receiverId,
        file = File(content),
        timestamp = timestamp
    )
}

fun FileMessage.toNetworkFileMessage(): NetworkFileMessage {
    return NetworkFileMessage(
        senderId = senderId,
        receiverId = receiverId,
        file = byteArrayOf(), // TODO
        timestamp = timestamp
    )
}

fun NetworkFileMessage.toFileMessage(): FileMessage {
    return FileMessage(
        senderId = senderId,
        receiverId = receiverId,
        file = File(""), // TODO
        timestamp = timestamp
    )
}