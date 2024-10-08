package com.flydrop2p.flydrop2p.domain.model.message

import com.flydrop2p.flydrop2p.data.local.message.MessageEntity

enum class MessageType {
    TEXT_MESSAGE, FILE_MESSAGE, AUDIO_MESSAGE
}

enum class MessageState {
    MESSAGE_SENT, MESSAGE_RECEIVED, MESSAGE_READ
}

sealed class Message : Comparable<Message> {
    abstract val messageId: Long
    abstract val senderId: Long
    abstract val receiverId: Long
    abstract val timestamp: Long
    abstract val messageState: MessageState

    abstract fun toMessageEntity(): MessageEntity

    override fun compareTo(other: Message): Int {
        return compareValuesBy(this, other, Message::timestamp)
    }
}

fun MessageEntity.toMessage(): Message {
    return when(messageType) {
        MessageType.TEXT_MESSAGE -> {
            toTextMessage()
        }

        MessageType.FILE_MESSAGE -> {
            toFileMessage()
        }

        MessageType.AUDIO_MESSAGE -> {
            toAudioMessage()
        }
    }
}