package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.domain.model.Account
import com.flydrop2p.flydrop2p.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var ipAddress: String?,
    val account: Account
) {
    val accountId: Int
        get() = account.accountId

    val profile: Profile
        get() = account.profile

    override fun equals(other: Any?): Boolean = other is Device && other.account == this.account

    override fun hashCode(): Int = account.hashCode()
}