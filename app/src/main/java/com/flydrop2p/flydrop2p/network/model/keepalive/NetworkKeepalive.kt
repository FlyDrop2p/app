package com.flydrop2p.flydrop2p.network.model.keepalive

import com.flydrop2p.flydrop2p.network.model.device.NetworkDevice
import kotlinx.serialization.Serializable

@Serializable
data class NetworkKeepalive(
    val networkDevices: List<NetworkDevice>
)