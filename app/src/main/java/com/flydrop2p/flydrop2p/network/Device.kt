package com.flydrop2p.flydrop2p.network

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: Long,
    var ipAddress: String?
)