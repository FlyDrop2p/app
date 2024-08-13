package com.flydrop2p.flydrop2p.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Long = -1,
    val username: String = "username"
) {
    override fun equals(other: Any?): Boolean = other is Profile && other.id == this.id

    override fun hashCode(): Int = this.id.hashCode()
}