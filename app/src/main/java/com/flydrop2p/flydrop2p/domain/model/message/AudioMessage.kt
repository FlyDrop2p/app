package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.network.model.message.NetworkAudioMessage

data class AudioMessage(
    override val messageId: Long,
    override val senderId: Long,
    override val receiverId: Long,
    override val timestamp: Long,
    override val messageState: MessageState,
    val fileName: String
) : Message() {
    constructor(networkAudioMessage: NetworkAudioMessage, messageState: MessageState, fileName: String) : this(
        messageId = networkAudioMessage.messageId,
        senderId = networkAudioMessage.senderId,
        receiverId = networkAudioMessage.receiverId,
        timestamp = networkAudioMessage.timestamp,
        messageState = messageState,
        fileName = fileName
    )

    override fun toMessageEntity(): MessageEntity {
        return MessageEntity(
            senderId = senderId,
            receiverId = receiverId,
            timestamp = timestamp,
            messageState = messageState,
            messageType = MessageType.AUDIO_MESSAGE,
            content = fileName
        )
    }
}

fun MessageEntity.toAudioMessage(): AudioMessage {
    return AudioMessage(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        timestamp = timestamp,
        messageState = messageState,
        fileName = content
    )
}