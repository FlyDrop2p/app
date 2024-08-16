package com.flydrop2p.flydrop2p.domain.model.contact

import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkContact
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val accountId: Int,
    val profile: Profile
) : Comparable<Contact> {
    val username: String
        get() = profile.username

    val imagePath: String?
        get() = profile.imagePath

    override fun compareTo(other: Contact): Int {
        return compareValuesBy(this, other, Contact::username)
    }
}

fun Contact.toContactEntity(): ContactEntity {
    return ContactEntity(
        accountId = accountId,
        username = profile.username,
        imagePath = imagePath
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        accountId = accountId,
        profile = Profile(username, imagePath)
    )
}

fun Contact.toNetworkContact(fileManager: FileManager): NetworkContact {
    return NetworkContact(
        accountId = accountId,
        networkProfile = profile.toNetworkProfile(fileManager)
    )
}

fun NetworkContact.toContact(fileManager: FileManager): Contact {
    return Contact(
        accountId = accountId,
        profile = networkProfile.toProfile(fileManager)
    )
}