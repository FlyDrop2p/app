package com.flydrop2p.flydrop2p.domain.model.contact

import com.flydrop2p.flydrop2p.data.local.profile.ProfileEntity
import com.flydrop2p.flydrop2p.network.model.contact.NetworkProfile
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val accountId: Int,
    val username: String,
    val imageFileName: String?
)

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        accountId = accountId,
        username = username,
        imageFileName = imageFileName
    )
}

fun ProfileEntity.toProfile(): Profile {
    return Profile(
        accountId = accountId,
        username = username,
        imageFileName = imageFileName
    )
}

fun Profile.toNetworkProfile(): NetworkProfile {
    return NetworkProfile(
        accountId = accountId,
        username = username,
        image = byteArrayOf() // TODO
    )
}

fun NetworkProfile.toProfile(): Profile {
    return Profile(
        accountId = accountId,
        username = username,
        imageFileName = accountId.toString()
    )
}