package com.flydrop2p.flydrop2p.network.model

import com.flydrop2p.flydrop2p.network.Device
import kotlinx.serialization.Serializable

@Serializable
data class NetworkKeepalive(
    val devices: List<Device>
) {
    override fun toString(): String =
        devices.toString()
}