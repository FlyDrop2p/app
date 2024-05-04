package com.example.flydrop2p.data

import android.content.Context
import com.example.flydrop2p.data.local.FlydropDatabase
import com.example.flydrop2p.data.repository.LocalChatInfoRepository
import com.example.flydrop2p.data.repository.LocalChatRepository
import com.example.flydrop2p.data.repository.LocalContactRepository
import com.example.flydrop2p.domain.repository.ChatRepository
import com.example.flydrop2p.domain.repository.ChatsInfoRepository
import com.example.flydrop2p.domain.repository.ContactRepository

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
        LocalChatRepository(FlydropDatabase.getDatabase(context).messageDao())
    }

    /**
     * Implementation for [ChatsInfoRepository]
     */
    override val chatsInfoRepository: ChatsInfoRepository by lazy {
        LocalChatInfoRepository(FlydropDatabase.getDatabase(context).chatInfoDao())
    }

    /**
     * Implementation for [ContactRepository]
     */
    override val contactRepository: ContactRepository by lazy {
        LocalContactRepository(FlydropDatabase.getDatabase(context).contactDao())
    }
}
