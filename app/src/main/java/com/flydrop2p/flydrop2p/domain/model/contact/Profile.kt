package com.flydrop2p.flydrop2p.domain.model.contact

import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkProfile
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val username: String,
    val imagePath: String?
)

fun Profile.toNetworkProfile(fileManager: FileManager): NetworkProfile {
    return NetworkProfile(
        username = username,
        image = fileManager.imagePathToByteArray(imagePath)
    )
}

fun NetworkProfile.toProfile(fileManager: FileManager): Profile {
    return Profile(
        username = username,
        imagePath = fileManager.byteArrayToImagePath(image, "${username}.JPG")
    )
}