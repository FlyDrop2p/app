package com.example.flydrop2p.ui.screen.Chat

import com.example.flydrop2p.domain.model.Chat


data class ChatViewState (
    val chat: Chat = Chat(0, "Default", emptyList()),
    val newMessage: String = ""
)