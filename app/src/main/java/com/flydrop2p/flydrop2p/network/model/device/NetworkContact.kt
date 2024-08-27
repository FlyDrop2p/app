package com.flydrop2p.flydrop2p.network.model.device

import kotlinx.serialization.Serializable

@Serializable
data class NetworkContact(
    val account: NetworkAccount,
    val profile: NetworkProfile?
) : Comparable<NetworkContact> {
    val accountId: Long
        get() = account.accountId

    val profileUpdateTimestamp: Long
        get() = account.profileUpdateTimestamp

    val username: String?
        get() = profile?.username

    val image: String?
        get() = profile?.imageBase64

    override fun compareTo(other: NetworkContact): Int {
        return compareValuesBy(this, other, NetworkContact::username)
    }
}