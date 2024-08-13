package com.flydrop2p.flydrop2p.data

import android.content.Context
import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.data.local.AppDataStore
import com.flydrop2p.flydrop2p.data.local.AppDatabase
import com.flydrop2p.flydrop2p.data.repository.ChatInfoLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ChatLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ContactLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ProfileLocalRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatInfoRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import com.flydrop2p.flydrop2p.network.NetworkManager

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val chatRepository: ChatRepository
    val chatInfoRepository: ChatInfoRepository
    val contactRepository: ContactRepository
    val profileRepository: ProfileRepository
    val networkManager: NetworkManager
}

/**
 * [AppContainer] implementation that provides instance of [ChatLocalRepository],
 * [ChatInfoLocalRepository], [ContactLocalRepository] and [NetworkManager].
 * This is done by following the design pattern of dependency injection
 */
class AppDataContainer(private val context: Context, activity: MainActivity) : AppContainer {
    /**
     * Implementation for [ChatRepository]
     */
    override val chatRepository: ChatRepository by lazy {
        ChatLocalRepository(AppDatabase.getDatabase(context).messageDao())
    }

    /**
     * Implementation for [ChatInfoRepository]
     */
    override val chatInfoRepository: ChatInfoRepository by lazy {
        ChatInfoLocalRepository(AppDatabase.getDatabase(context).chatInfoDao(), AppDatabase.getDatabase(context).chatContactsDao())
    }

    /**
     * Implementation for [ContactRepository]
     */
    override val contactRepository: ContactRepository by lazy {
        ContactLocalRepository(AppDatabase.getDatabase(context).contactDao(), AppDatabase.getDatabase(context).chatContactsDao())
    }

    /**
     * Implementation for [ProfileRepository]
     */
    override val profileRepository: ProfileRepository by lazy {
        ProfileLocalRepository(AppDataStore.getProfileRepository(context))
    }

    /**
     * Implementation for [NetworkManager]
     */
    override val networkManager: NetworkManager = NetworkManager(activity)
}
