package com.flydrop2p.flydrop2p.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfileRequest(
    val senderId: Int,
    val receiverId: Int
)