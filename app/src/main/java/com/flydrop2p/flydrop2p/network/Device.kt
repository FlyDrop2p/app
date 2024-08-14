package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String? = null,
    var accountId: Int,
    var profile: Profile
) {
    val username: String
        get() = profile.username
}

fun Device.toContact(): Contact {
    return Contact(accountId, profile)
}