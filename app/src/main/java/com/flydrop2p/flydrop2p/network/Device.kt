package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String?,
    val profile: Profile
) {
    val id: Long
        get() = profile.id

    val username: String
        get() = profile.username

    override fun equals(other: Any?): Boolean = other is Device && other.profile == this.profile

    override fun hashCode(): Int = profile.hashCode()
}