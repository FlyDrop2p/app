package com.flydrop2p.flydrop2p.network.model.contact

import kotlinx.serialization.Serializable

@Serializable
data class NetworkContact(
    val account: NetworkAccount,
    val profile: NetworkProfile?
) : Comparable<NetworkContact> {
    val accountId: Long
        get() = account.accountId

    val profileUpdate: Long
        get() = account.profileUpdate

    val username: String?
        get() = profile?.username

    val image: String?
        get() = profile?.image

    override fun compareTo(other: NetworkContact): Int {
        return compareValuesBy(this, other, NetworkContact::username)
    }
}