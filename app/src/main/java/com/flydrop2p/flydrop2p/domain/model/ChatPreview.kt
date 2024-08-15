package com.flydrop2p.flydrop2p.domain.model

data class ChatPreview(
    val contact: Contact,
    val lastMessage: TextMessage?
) : Comparable<ChatPreview> {
    override fun compareTo(other: ChatPreview): Int {
        return compareValuesBy(this, other, ChatPreview::lastMessage, ChatPreview::contact)
    }
}