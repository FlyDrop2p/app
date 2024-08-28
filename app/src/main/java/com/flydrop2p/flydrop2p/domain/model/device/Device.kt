package com.flydrop2p.flydrop2p.domain.model.device

import com.flydrop2p.flydrop2p.network.model.device.NetworkDevice

data class Device(
    var ipAddress: String?,
    var keepalive: Long,
    val account: Account,
    val profile: Profile
) {
    constructor(networkDevice: NetworkDevice, profile: Profile)
            : this(networkDevice.ipAddress, networkDevice.keepalive, networkDevice.account.toAccount(), profile)
}

fun Device.toNetworkDevice(): NetworkDevice {
    return NetworkDevice(
        ipAddress = ipAddress,
        keepalive = keepalive,
        account = account.toNetworkAccount()
    )
}