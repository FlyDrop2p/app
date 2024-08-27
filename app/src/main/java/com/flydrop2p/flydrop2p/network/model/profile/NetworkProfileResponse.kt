package com.flydrop2p.flydrop2p.network.model.profile

import com.flydrop2p.flydrop2p.network.model.device.NetworkProfile
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfileResponse(
    val senderId: Long,
    val receiverId: Long,
    val profile: NetworkProfile
)