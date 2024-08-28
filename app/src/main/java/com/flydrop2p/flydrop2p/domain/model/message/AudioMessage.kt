package com.flydrop2p.flydrop2p.domain.model.message

import android.content.Context
import android.media.MediaMetadataRetriever
import com.flydrop2p.flydrop2p.data.local.message.MessageEntity
import com.flydrop2p.flydrop2p.network.model.message.NetworkAudioMessage
import java.io.File

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

    fun getFilePath(context: Context): String {
        val fileDir = context.filesDir
        return File(fileDir, fileName).absolutePath
    }

    fun getAudioDuration(filePath: String): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filePath)
        val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()

        return durationStr?.toLong() ?: 0L
    }

    fun formatDuration(context: Context): String {
        val durationInMillis = getAudioDuration(getFilePath(context))
        val seconds = (durationInMillis / 1000) % 60
        val minutes = (durationInMillis / (1000 * 60)) % 60
        val hours = (durationInMillis / (1000 * 60 * 60)) % 24

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun formatDuration(duration: Int): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        val hours = (duration / (1000 * 60 * 60)) % 24

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
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