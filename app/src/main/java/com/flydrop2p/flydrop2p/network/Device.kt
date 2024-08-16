package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toContact
import com.flydrop2p.flydrop2p.domain.model.contact.toNetworkContact
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkDevice
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
}

fun Device.toNetworkDevice(fileManager: FileManager): NetworkDevice {
    return NetworkDevice(
        ipAddress = ipAddress,
        networkContact = contact.toNetworkContact(fileManager)
    )
}

fun NetworkDevice.toDevice(fileManager: FileManager): Device {
    return Device(
        ipAddress = ipAddress,
        contact = networkContact.toContact(fileManager)
    )
}