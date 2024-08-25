package com.flydrop2p.flydrop2p.network.model.message

import kotlinx.serialization.Serializable

@Serializable
class NetworkFileMessage(
    val senderId: Int,
    val receiverId: Int,
    val timestamp: Long,
    val file: String
)