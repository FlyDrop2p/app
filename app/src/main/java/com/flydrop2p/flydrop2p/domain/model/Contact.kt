package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val contactId: Int = -1,
    val profile: Profile = Profile()
) {
    val username: String
        get() = profile.username

    val imageFilePath: String?
        get() = profile.imageFilePath

    override fun equals(other: Any?): Boolean =
        other is Contact && other.contactId == this.contactId

    override fun hashCode(): Int =
        this.contactId.hashCode()
}

fun Contact.toContactEntity(): ContactEntity {
    return ContactEntity(
        contactId = contactId,
        username = username,
        imageFilePath = imageFilePath
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        contactId = contactId,
        profile = Profile(username, imageFilePath)
    )
}