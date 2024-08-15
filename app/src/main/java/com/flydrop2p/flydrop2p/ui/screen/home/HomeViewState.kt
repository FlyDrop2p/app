package com.flydrop2p.flydrop2p.ui.screen.home

import com.flydrop2p.flydrop2p.domain.model.ChatPreview

data class HomeViewState(
    val chatPreviews: List<ChatPreview> = listOf()
)
