package com.example.flydrop2p.ui.screen.Home

import com.example.flydrop2p.domain.model.ChatInfo

data class HomeViewState(
    val chats: List<ChatInfo> = listOf()
)
