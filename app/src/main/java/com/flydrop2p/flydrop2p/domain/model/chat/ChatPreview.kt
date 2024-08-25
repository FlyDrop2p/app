package com.flydrop2p.flydrop2p.domain.model.chat

import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.message.Message

data class ChatPreview(
    val contact: Contact,
    val unreadMessagesCount: Int,
    val lastMessage: Message?
) : Comparable<ChatPreview> {
    override fun compareTo(other: ChatPreview): Int {
        return compareValuesBy(this, other, ChatPreview::lastMessage, ChatPreview::contact, ChatPreview::unreadMessagesCount)
    }
}