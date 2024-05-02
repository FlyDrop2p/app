package com.example.flydrop2p.domain.model

data class Message(
    val messageId: Int,
    val message: String,
    val timestamp: String,
    val senderId: Int,
    val receiverId: Int
)
