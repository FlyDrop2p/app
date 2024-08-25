package com.flydrop2p.flydrop2p.domain.model.message

import android.content.Context
import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.data.local.message.MessageType
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage
import java.io.File

data class FileMessage(
    override val senderId: Int,
    override val receiverId: Int,
    override val timestamp: Long,
    val file: File
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
        file = file.readBytes(),
        timestamp = timestamp
    )
}

fun NetworkFileMessage.toFileMessage(context: Context): FileMessage {
    val file = File(context.filesDir, "${System.currentTimeMillis()}_file")
    file.writeBytes(this.file)

    return FileMessage(
        senderId = senderId,
        receiverId = receiverId,
        file = file,
        timestamp = timestamp
    )
}