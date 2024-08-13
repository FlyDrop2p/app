package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String?,
    val contact: Contact
) {
    val contactId: Int
        get() = contact.contactId

    val profile: Profile
        get() = contact.profile

    override fun equals(other: Any?): Boolean = other is Device && other.profile == this.profile

    override fun hashCode(): Int = profile.hashCode()
}