package com.flydrop2p.flydrop2p.ui.screen.chat

import com.flydrop2p.flydrop2p.data.local.MessageEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo


data class ChatViewState(
    val chatInfo: ChatInfo = ChatInfo(0, "Loading..."),
    val messageList: MutableList<MessageEntity> = mutableListOf()
)