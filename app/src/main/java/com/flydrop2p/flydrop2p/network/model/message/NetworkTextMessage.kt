package com.flydrop2p.flydrop2p.network.model.message

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTextMessage(
    val senderId: Int,
    val receiverId: Int,
    val timestamp: Long,
    val text: String,
)