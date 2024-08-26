package com.flydrop2p.flydrop2p.domain.model.contact

import com.flydrop2p.flydrop2p.data.local.profile.ProfileEntity
import com.flydrop2p.flydrop2p.network.model.contact.NetworkProfile

data class Profile(
    val accountId: Long,
    val username: String,
    val imageFileName: String?
) {
    constructor(networkProfile: NetworkProfile, imageFileName: String?)
        : this(networkProfile.accountId, networkProfile.username, imageFileName)
}

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