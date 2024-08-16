package com.flydrop2p.flydrop2p.network.model.keepalive

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfile(
    val username: String,
    val image: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if(this === other) {
            return true
        }

        if(other is NetworkProfile && other.username == this.username && other.image.contentEquals(this.image)) {
            return true
        }

        return false
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}