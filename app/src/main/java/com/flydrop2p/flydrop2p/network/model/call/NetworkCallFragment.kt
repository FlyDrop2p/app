package com.flydrop2p.flydrop2p.network.model.call

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCallFragment(
    val audioBase64: String
)