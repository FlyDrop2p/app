package com.flydrop2p.flydrop2p.network.model.contact

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAccount(
    val accountId: Int,
    val profileUpdate: Long
)