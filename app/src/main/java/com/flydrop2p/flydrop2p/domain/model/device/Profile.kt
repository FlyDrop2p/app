package com.flydrop2p.flydrop2p.domain.model.device

import com.flydrop2p.flydrop2p.data.local.profile.ProfileEntity
import com.flydrop2p.flydrop2p.network.model.device.NetworkProfile

data class Profile(
    val accountId: Long,
    val updateTimestamp: Long,
    val username: String,
    val imageFileName: String?
) {
    constructor(networkProfile: NetworkProfile, imageFileName: String?)
        : this(networkProfile.accountId, networkProfile.updateTimestamp, networkProfile.username, imageFileName)
}

fun Profile.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        accountId = accountId,
        updateTimestamp = updateTimestamp,
        username = username,
        imageFileName = imageFileName
    )
}

fun ProfileEntity.toProfile(): Profile {
    return Profile(
        accountId = accountId,
        updateTimestamp = updateTimestamp,
        username = username,
        imageFileName = imageFileName
    )
}