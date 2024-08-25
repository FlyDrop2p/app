package com.flydrop2p.flydrop2p.network.model.message

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTextMessage(
    val messageId: Long,
    val senderId: Long,
    val receiverId: Long,
    val timestamp: Long,
    val text: String,
)