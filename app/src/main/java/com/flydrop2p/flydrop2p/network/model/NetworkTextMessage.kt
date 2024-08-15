package com.flydrop2p.flydrop2p.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTextMessage(
    val senderId: Int,
    val content: String,
    val timestamp: Long
)