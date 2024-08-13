package com.flydrop2p.flydrop2p.domain.model

import com.flydrop2p.flydrop2p.data.local.contact.ContactEntity
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Account(
    val accountId: Int = Random.nextInt(),
    val profile: Profile = Profile()
) {
    val username: String
        get() = profile.username

    val imageFilePath: String?
        get() = profile.imageFilePath

    override fun equals(other: Any?): Boolean =
        other is Account && other.accountId == this.accountId

    override fun hashCode(): Int =
        this.accountId.hashCode()
}

fun Account.toContactEntity(): ContactEntity {
    return ContactEntity(
        accountId = accountId,
        username = username,
        imageFilePath = imageFilePath
    )
}

fun ContactEntity.toAccount(): Account {
    return Account(
        accountId = accountId,
        profile = Profile(username, imageFilePath)
    )
}