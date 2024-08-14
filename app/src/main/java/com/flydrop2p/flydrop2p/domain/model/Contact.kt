package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity

data class Contact(
    val accountId: Int,
    val profile: Profile
) {
    val username: String
        get() = profile.username

    override fun equals(other: Any?): Boolean {
        return other is Contact && other.accountId == accountId
    }

    override fun hashCode(): Int {
        return accountId.hashCode()
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