package com.flydrop2p.flydrop2p.domain.model.device

data class Contact(
    val account: Account,
    val profile: Profile?
) : Comparable<Contact> {
    val accountId: Long
        get() = account.accountId

    val profileUpdateTimestamp: Long
        get() = account.profileUpdateTimestamp

    val username: String?
        get() = profile?.username

    val imageFileName: String?
        get() = profile?.imageFileName

    override fun compareTo(other: Contact): Int {
        return compareValuesBy(this, other, Contact::username)
    }
}