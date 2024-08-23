package com.flydrop2p.flydrop2p.ui.screen.chat

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.message.Message


data class ChatViewState(
    val contact: Contact = Contact(Account(0, 0), Profile(0, "", null)),
    val messages: List<Message> = listOf()
)