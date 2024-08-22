package com.flydrop2p.flydrop2p.network.model.keepalive

import com.flydrop2p.flydrop2p.network.model.contact.NetworkAccount
import com.flydrop2p.flydrop2p.network.model.contact.NetworkContact
import com.flydrop2p.flydrop2p.network.model.contact.NetworkProfile
import kotlinx.serialization.Serializable

@Serializable
data class NetworkDevice(
    val ipAddress: String?,
    val account: NetworkAccount
)