package com.flydrop2p.flydrop2p.network.model.message

import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import kotlinx.serialization.Serializable

@Serializable
class NetworkFileMessage(
    val senderId: Long,
    val receiverId: Long,
    val timestamp: Long,
    val file: String
) {
    constructor(fileMessage: FileMessage, file: String)
        : this(fileMessage.senderId, fileMessage.receiverId, fileMessage.timestamp, file)
}