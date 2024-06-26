package com.flydrop2p.flydrop2p.data

import android.content.Context
import com.flydrop2p.flydrop2p.data.local.FlyDropDatabase
import com.flydrop2p.flydrop2p.data.repository.LocalChatInfoRepository
import com.flydrop2p.flydrop2p.data.repository.LocalChatRepository
import com.flydrop2p.flydrop2p.data.repository.LocalContactRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatsInfoRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val chatRepository: ChatRepository
    val chatsInfoRepository: ChatsInfoRepository
    val contactRepository: ContactRepository
}

/**
 * [AppContainer] implementation that provides instance of [LocalChatRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ChatRepository]
     */
    override val chatRepository: ChatRepository by lazy {
        LocalChatRepository(FlyDropDatabase.getDatabase(context).messageDao())
    }

    /**
     * Implementation for [ChatsInfoRepository]
     */
    override val chatsInfoRepository: ChatsInfoRepository by lazy {
        LocalChatInfoRepository(FlyDropDatabase.getDatabase(context).chatInfoDao())
    }

    /**
     * Implementation for [ContactRepository]
     */
    override val contactRepository: ContactRepository by lazy {
        LocalContactRepository(FlyDropDatabase.getDatabase(context).contactDao())
    }
}
