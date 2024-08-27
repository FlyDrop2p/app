package com.flydrop2p.flydrop2p.network.model.device

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAccount(
    val accountId: Long,
    val profileUpdateTimestamp: Long
)