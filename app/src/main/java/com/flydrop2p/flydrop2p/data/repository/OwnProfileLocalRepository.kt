package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import kotlinx.coroutines.flow.Flow

class OwnProfileLocalRepository(private val ownProfileDataStore: DataStore<Profile>) : OwnProfileRepository {
    override val profile: Flow<Profile>
        get() = ownProfileDataStore.data

    override suspend fun setUsername(username: String) {
        ownProfileDataStore.updateData { it.copy(username = username) }
    }

    override suspend fun setImageFilePath(imageFilePath: String) {
        ownProfileDataStore.updateData { it.copy(imageFileName = imageFilePath) }
    }
}