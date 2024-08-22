package com.flydrop2p.flydrop2p.network.model.profile

import com.flydrop2p.flydrop2p.network.model.contact.NetworkProfile
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfileResponse(
    val senderId: Int,
    val receiverId: Int,
    val profile: NetworkProfile
)