package com.flydrop2p.flydrop2p.network.model.contact

import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfile(
    val accountId: Int,
    val username: String,
    val image: String?
) {

    constructor(profile: Profile, image: String?)
            : this(profile.accountId, profile.username, image)
}