package com.flydrop2p.flydrop2p.network.model.message

import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import kotlinx.serialization.Serializable

@Serializable
class NetworkFileMessage(
    val messageId: Long,
    val senderId: Long,
    val receiverId: Long,
    val timestamp: Long,
    val fileName: String,
    val fileBase64: String
) {
    constructor(fileMessage: FileMessage, fileBase64: String)
        : this(fileMessage.messageId, fileMessage.senderId, fileMessage.receiverId, fileMessage.timestamp, fileMessage.fileName, fileBase64)
}