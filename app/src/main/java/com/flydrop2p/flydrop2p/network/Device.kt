package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toAccount
import com.flydrop2p.flydrop2p.domain.model.contact.toNetworkAccount
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkDevice

data class Device(
    var ipAddress: String?,
    val account: Account,
    val profile: Profile
) {
    constructor(networkDevice: NetworkDevice, profile: Profile)
            : this(networkDevice.ipAddress, networkDevice.account.toAccount(), profile)
}

fun Device.toNetworkDevice(): NetworkDevice {
    return NetworkDevice(
        ipAddress = ipAddress,
        account = account.toNetworkAccount()
    )
}