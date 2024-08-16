package com.flydrop2p.flydrop2p.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.flydrop2p.flydrop2p.data.local.serializer.AccountSerializer
import com.flydrop2p.flydrop2p.data.local.serializer.ProfileSerializer
import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Profile

abstract class AppDataStore {
    companion object {
        private val Context.accountDataStore by dataStore(
            fileName = "account.json",
            serializer = AccountSerializer
        )

        private val Context.profileDataStore by dataStore(
            fileName = "profile.json",
            serializer = ProfileSerializer
        )

        fun getAccountRepository(context: Context): DataStore<Account> {
            return context.accountDataStore
        }

        fun getProfileRepository(context: Context): DataStore<Profile> {
            return context.profileDataStore
        }
    }
}