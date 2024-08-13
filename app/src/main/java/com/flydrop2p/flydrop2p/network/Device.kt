package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.R
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: Long,
    var ipAddress: String?,
    var name: String? = "Finto",
    val imgId: Int = R.drawable.account_circle_24px
) {
    override fun equals(other: Any?): Boolean {
        return other is Device && other.id == this.id
    }

    override fun hashCode(): Int = id.hashCode()
}