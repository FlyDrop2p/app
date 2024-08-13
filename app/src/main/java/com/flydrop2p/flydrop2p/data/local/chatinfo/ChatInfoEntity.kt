package com.flydrop2p.flydrop2p.data.local.chatinfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val chatId: Int = 0,
    val chatType: ChatType,
    val name: String,
    val imageFilePath: String?,
    val creationTimestamp: Long,
)
