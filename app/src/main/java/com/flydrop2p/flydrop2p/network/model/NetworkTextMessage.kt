package com.flydrop2p.flydrop2p.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTextMessage(
    val senderId: Int,
    val receiverId: Int,
    val text: String,
    val timestamp: Long
)