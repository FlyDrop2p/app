package com.flydrop2p.flydrop2p.data.repository

import androidx.datastore.core.DataStore
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class OwnProfileLocalRepository(private val ownProfileDataStore: DataStore<Profile>) : OwnProfileRepository {
    override fun getProfileAsFlow(): Flow<Profile> {
        return ownProfileDataStore.data
    }

    override suspend fun getProfile(): Profile {
        return ownProfileDataStore.data.first()
    }

    override suspend fun setUsername(username: String) {
        ownProfileDataStore.updateData { it.copy(username = username) }
    }

    override suspend fun setImageFileName(imageFileName: String) {
        ownProfileDataStore.updateData { it.copy(imageFileName = imageFileName) }
    }
}