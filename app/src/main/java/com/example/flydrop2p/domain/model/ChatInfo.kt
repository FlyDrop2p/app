package com.example.flydrop2p.domain.model

data class ChatInfo(
    val id: Int,
    val name: String,
    val messages: List<Message>,
)
