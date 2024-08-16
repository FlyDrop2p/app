package com.flydrop2p.flydrop2p.network.model.keepalive

import kotlinx.serialization.Serializable

@Serializable
data class NetworkContact(
    val accountId: Int,
    val networkProfile: NetworkProfile
) {
    val username: String
        get() = networkProfile.username

    val image: ByteArray?
        get() = networkProfile.image
}