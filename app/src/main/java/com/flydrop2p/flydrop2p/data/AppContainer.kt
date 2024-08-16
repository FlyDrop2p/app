package com.flydrop2p.flydrop2p.data

import android.content.Context
import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.data.local.AppDataStore
import com.flydrop2p.flydrop2p.data.local.AppDatabase
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.data.repository.AccountLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ChatLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ContactLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ProfileLocalRepository
import com.flydrop2p.flydrop2p.domain.repository.AccountRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import com.flydrop2p.flydrop2p.network.NetworkManager

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val chatRepository: ChatRepository
    val contactRepository: ContactRepository
    val accountRepository: AccountRepository
    val profileRepository: ProfileRepository
    val fileManager: FileManager
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
        ChatLocalRepository(AppDatabase.getDatabase(context).contactDao(), AppDatabase.getDatabase(context).messageDao())
    }

    /**
     * Implementation for [ContactRepository]
     */
    override val contactRepository: ContactRepository by lazy {
        ContactLocalRepository(AppDatabase.getDatabase(context).contactDao())
    }

    /**
     * Implementation for [AccountRepository]
     */
    override val accountRepository: AccountRepository by lazy {
        AccountLocalRepository(AppDataStore.getAccountRepository(context))
    }

    /**
     * Implementation for [ProfileRepository]
     */
    override val profileRepository: ProfileRepository by lazy {
        ProfileLocalRepository(AppDataStore.getProfileRepository(context))
    }

    /**
     * Implementation for [FileManager]
     */
    override val fileManager: FileManager = FileManager(activity)

    /**
     * Implementation for [NetworkManager]
     */
    override val networkManager: NetworkManager = NetworkManager(activity, accountRepository, profileRepository, chatRepository, contactRepository, fileManager)
}
