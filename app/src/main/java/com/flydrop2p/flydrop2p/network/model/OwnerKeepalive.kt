package com.flydrop2p.flydrop2p.network.model

import com.flydrop2p.flydrop2p.network.Device
import kotlinx.serialization.Serializable

@Serializable
class OwnerKeepalive(
    val device: Device
) {
    override fun toString(): String =
        device.toString()
}