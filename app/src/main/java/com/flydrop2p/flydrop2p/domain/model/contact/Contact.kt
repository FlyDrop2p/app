package com.flydrop2p.flydrop2p.domain.model.contact

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val account: Account,
    val profile: Profile?
) : Comparable<Contact> {
    val accountId: Int
        get() = account.accountId

    val profileUpdate: Long
        get() = account.profileUpdate

    val username: String?
        get() = profile?.username

    val imageFileName: String?
        get() = profile?.imageFileName

    override fun compareTo(other: Contact): Int {
        return compareValuesBy(this, other, Contact::username)
    }
}