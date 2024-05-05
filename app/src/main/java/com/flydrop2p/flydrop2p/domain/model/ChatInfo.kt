package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.data.local.ChatInfoEntity

data class ChatInfo(
    val id: Int,
    val name: String,
    val imgId: Int = R.drawable.account_circle_24px
)

fun ChatInfo.toChatInfoEntity(): ChatInfoEntity {
    return ChatInfoEntity(
        name = name,
        imgId = imgId
    )
}

fun ChatInfoEntity.toChatInfo(): ChatInfo {
    return ChatInfo(
        id = id,
        name = name,
        imgId = imgId
    )
}
