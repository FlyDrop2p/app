package com.flydrop2p.flydrop2p.network

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: Long,
    var ipAddress: String?
) {
    override fun equals(other: Any?): Boolean {
        return other is Device && other.id == this.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (id.hashCode())
        return result
    }
}