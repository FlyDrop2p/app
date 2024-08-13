package com.flydrop2p.flydrop2p.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.flydrop2p.flydrop2p.data.local.profile.ProfileSerializer
import com.flydrop2p.flydrop2p.domain.model.Profile

abstract class AppDataStore {
    companion object {
        val Context.profileDataStore by dataStore(
            fileName = "profile",
            serializer = ProfileSerializer
        )

        fun getProfileRepository(context: Context): DataStore<Profile> {
            return context.profileDataStore
        }
    }
}