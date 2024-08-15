package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String? = null,
    var contact: Contact
) {
    val accountId: Int
        get() = contact.accountId
    
    val profile: Profile
        get() = contact.profile
    
    val username: String
        get() = contact.username
}