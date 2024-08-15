package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.domain.model.Profile
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileLocalRepository(private val profileDataStore: DataStore<Profile>) : ProfileRepository {
    override val profile: Flow<Profile>
        get() = profileDataStore.data

    override suspend fun setUsername(username: String) {
        profileDataStore.updateData { it.copy(username = username) }
    }

    override suspend fun setImageFilePath(imageFilePath: String) {
        profileDataStore.updateData { it.copy(imageFilePath = imageFilePath) }
    }
}