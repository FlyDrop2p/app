package com.flydrop2p.flydrop2p.network.model

import kotlinx.serialization.Serializable

@Serializable
class NetworkFileMessage(
    val senderId: Int,
    val file: ByteArray,
    val timestamp: Long
)