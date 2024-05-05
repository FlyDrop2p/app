package com.flydrop2p.flydrop2p.ui.screen.Chat

import com.flydrop2p.flydrop2p.domain.model.Chat


data class ChatViewState (
    val chat: Chat = Chat(0, "Default", emptyList()),
    val newMessage: String = ""
)