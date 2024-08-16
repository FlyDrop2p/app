package com.flydrop2p.flydrop2p.network.model.keepalive

import kotlinx.serialization.Serializable

@Serializable
data class NetworkDevice(
    val ipAddress: String?,
    val networkContact: NetworkContact
) {
    val accountId: Int
        get() = networkContact.accountId

    val networkProfile: NetworkProfile
        get() = networkContact.networkProfile
}