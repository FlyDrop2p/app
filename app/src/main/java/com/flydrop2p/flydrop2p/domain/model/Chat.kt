package com.flydrop2p.flydrop2p.domain.model

data class Chat(
    val id: Int,
    val name: String,
    var messages: List<Message>,
)
