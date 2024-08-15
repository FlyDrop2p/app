package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val accountId: Int,
    val profile: Profile
) : Comparable<Contact> {
    val username: String
        get() = profile.username

    override fun equals(other: Any?): Boolean {
        return other is Contact && other.accountId == accountId
    }

    override fun hashCode(): Int {
        return accountId.hashCode()
    }

    override fun compareTo(other: Contact): Int {
        return compareValuesBy(this, other, Contact::username)
    }
}

fun Contact.toContactEntity(): ContactEntity {
    return ContactEntity(
        accountId = accountId,
        username = profile.username
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        accountId = accountId,
        profile = Profile(username)
    )
}