package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toAccount
import com.flydrop2p.flydrop2p.domain.model.contact.toNetworkAccount
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkDevice
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String?,
    var contact: Contact
) {
    constructor(networkDevice: NetworkDevice, profile: Profile?)
            : this(networkDevice.ipAddress, Contact(networkDevice.account.toAccount(), profile))

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