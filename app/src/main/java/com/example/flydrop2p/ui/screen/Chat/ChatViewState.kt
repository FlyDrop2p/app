package com.example.flydrop2p.ui.screen.Chat

import com.example.flydrop2p.domain.model.ChatInfo

data class ChatViewState (
    val chatInfo: ChatInfo = ChatInfo(0, "", emptyList()),
    val newMessage: String = ""
)