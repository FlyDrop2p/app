package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatInfoEntity
import com.flydrop2p.flydrop2p.data.local.chatinfo.ChatType

data class ChatInfo(
    val chatId: Int,
    val chatType: ChatType,
    val name: String,
    val imageFilePath: String?,
    val creationTimestamp: Long,
)

fun ChatInfo.toChatInfoEntity(): ChatInfoEntity {
    return ChatInfoEntity(
        chatId = chatId,
        chatType = chatType,
        name = name,
        imageFilePath = imageFilePath,
        creationTimestamp = creationTimestamp
    )
}

fun ChatInfoEntity.toChatInfo(): ChatInfo {
    return ChatInfo(
        chatId = chatId,
        chatType = chatType,
        name = name,
        imageFilePath = imageFilePath,
        creationTimestamp = creationTimestamp
    )
}
