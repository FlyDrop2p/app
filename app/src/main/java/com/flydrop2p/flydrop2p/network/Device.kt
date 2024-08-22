package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toContact
import com.flydrop2p.flydrop2p.domain.model.contact.toNetworkAccount
import com.flydrop2p.flydrop2p.domain.model.contact.toNetworkContact
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkDevice
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String?,
    var contact: Contact
) {
    val account: Account
        get() = contact.account
    
    val profile: Profile?
        get() = contact.profile
}

fun Device.toNetworkDevice(): NetworkDevice {
    return NetworkDevice(
        ipAddress = ipAddress,
        account = account.toNetworkAccount()
    )
}