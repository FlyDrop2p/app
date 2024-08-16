package com.flydrop2p.flydrop2p.network.model.keepalive

import com.flydrop2p.flydrop2p.network.Device
import kotlinx.serialization.Serializable

@Serializable
data class NetworkKeepalive(
    val networkDevices: List<NetworkDevice>
) {
    override fun toString(): String =
        networkDevices.toString()
}