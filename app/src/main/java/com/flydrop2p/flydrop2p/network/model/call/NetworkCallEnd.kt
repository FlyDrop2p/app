package com.flydrop2p.flydrop2p.network.model.call

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCallEnd(
    val senderId: Long,
    val receiverId: Long,
    val ack: Boolean
)