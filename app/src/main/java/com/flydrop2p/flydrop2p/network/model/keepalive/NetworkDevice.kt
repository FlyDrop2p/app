package com.flydrop2p.flydrop2p.network.model.keepalive

import com.flydrop2p.flydrop2p.network.model.contact.NetworkAccount
import kotlinx.serialization.Serializable

@Serializable
data class NetworkDevice(
    val ipAddress: String?,
    val account: NetworkAccount
)