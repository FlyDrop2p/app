package com.flydrop2p.flydrop2p.domain.model

data class Chat(
    val chatInfo: ChatInfo,
    var messages: MutableList<Message>,
)
