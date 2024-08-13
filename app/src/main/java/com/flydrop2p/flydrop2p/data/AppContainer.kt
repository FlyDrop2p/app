package com.flydrop2p.flydrop2p.data

import android.content.Context
import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.data.local.AppDataStore
import com.flydrop2p.flydrop2p.data.local.AppDatabase
import com.flydrop2p.flydrop2p.data.repository.LocalChatInfoRepository
import com.flydrop2p.flydrop2p.data.repository.LocalChatRepository
import com.flydrop2p.flydrop2p.data.repository.LocalContactRepository
import com.flydrop2p.flydrop2p.data.repository.LocalProfileRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatsInfoRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import com.flydrop2p.flydrop2p.network.NetworkManager

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val chatRepository: ChatRepository
    val chatsInfoRepository: ChatsInfoRepository
    val contactRepository: ContactRepository
    val profileRepository: ProfileRepository
    val networkManager: NetworkManager
}

/**
 * [AppContainer] implementation that provides instance of [LocalChatRepository],
 * [LocalChatInfoRepository], [LocalContactRepository] and [NetworkManager].
 * This is done by following the design pattern of dependency injection
 */
class AppDataContainer(private val context: Context, activity: MainActivity) : AppContainer {
    /**
     * Implementation for [ChatRepository]
     */
    override val chatRepository: ChatRepository by lazy {
        LocalChatRepository(AppDatabase.getDatabase(context).messageDao())
    }

    /**
     * Implementation for [ChatsInfoRepository]
     */
    override val chatsInfoRepository: ChatsInfoRepository by lazy {
        LocalChatInfoRepository(AppDatabase.getDatabase(context).chatInfoDao())
    }

    /**
     * Implementation for [ContactRepository]
     */
    override val contactRepository: ContactRepository by lazy {
        LocalContactRepository(AppDatabase.getDatabase(context).contactDao())
    }

    /**
     * Implementation for [ProfileRepository]
     */
    override val profileRepository: ProfileRepository by lazy {
        LocalProfileRepository(AppDataStore.getProfileRepository(context))
    }

    /**
     * Implementation for [NetworkManager]
     */
    override val networkManager: NetworkManager = NetworkManager(activity)
}
