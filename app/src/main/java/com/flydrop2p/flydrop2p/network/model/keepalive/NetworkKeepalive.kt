package com.flydrop2p.flydrop2p.network.model.keepalive

import kotlinx.serialization.Serializable

@Serializable
data class NetworkKeepalive(
    val networkDevices: List<NetworkDevice>
)