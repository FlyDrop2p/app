package com.flydrop2p.flydrop2p.network.model.message

import com.flydrop2p.flydrop2p.domain.model.message.AudioMessage
import kotlinx.serialization.Serializable

@Serializable
class NetworkAudioMessage(
    val messageId: Long,
    val senderId: Long,
    val receiverId: Long,
    val timestamp: Long,
    val audioBase64: String
) {
    constructor(audioMessage: AudioMessage, audioBase64: String)
            : this(audioMessage.messageId, audioMessage.senderId, audioMessage.receiverId, audioMessage.timestamp, audioBase64)
}