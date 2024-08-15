package com.flydrop2p.flydrop2p.ui.screen.chat

import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Profile
import com.flydrop2p.flydrop2p.domain.model.message.Message


data class ChatViewState(
    val contact: Contact = Contact(0, Profile("username")),
    val messages: List<Message> = listOf()
)