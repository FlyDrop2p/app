package com.flydrop2p.flydrop2p.ui.screen.Home

import com.flydrop2p.flydrop2p.domain.model.ChatInfo

data class HomeViewState(
    val chats: List<ChatInfo> = listOf()
)
