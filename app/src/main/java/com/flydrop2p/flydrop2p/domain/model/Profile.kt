package com.flydrop2p.flydrop2p.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val username: String = android.os.Build.MODEL.toString(),
    val imageFilePath: String? = null
)