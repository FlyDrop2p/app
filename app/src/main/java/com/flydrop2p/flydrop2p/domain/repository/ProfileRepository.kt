package com.flydrop2p.flydrop2p.domain.repository

import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>

    suspend fun setUsername(username: String)

    suspend fun setImageFilePath(imageFilePath: String)
}