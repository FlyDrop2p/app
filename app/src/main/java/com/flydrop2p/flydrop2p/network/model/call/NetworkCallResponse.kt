package com.flydrop2p.flydrop2p.network.model.call

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCallResponse(
    val senderId: Long,
    val receiverId: Long,
    val accepted: Boolean
)