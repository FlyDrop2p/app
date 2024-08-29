package com.flydrop2p.flydrop2p.ui.screen.info

import com.flydrop2p.flydrop2p.domain.model.device.Profile
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.Message

data class InfoViewState(
    val profile: Profile = Profile(0, 0, "username", null),
    val mediaMessages: List<FileMessage> = listOf()
)
