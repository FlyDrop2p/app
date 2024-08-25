package com.flydrop2p.flydrop2p.network.model.contact

import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfile(
    val accountId: Long,
    val username: String,
    val imageBase64: String?
) {
    constructor(profile: Profile, imageBase64: String?)
            : this(profile.accountId, profile.username, imageBase64)
}