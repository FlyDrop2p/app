package com.flydrop2p.flydrop2p.network.model.device

import kotlinx.serialization.Serializable

@Serializable
data class NetworkDevice(
    val ipAddress: String?,
    val keepalive: Long,
    val account: NetworkAccount
)