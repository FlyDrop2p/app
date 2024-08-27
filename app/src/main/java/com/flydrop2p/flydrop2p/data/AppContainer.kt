package com.flydrop2p.flydrop2p.data

import android.content.Context
import com.flydrop2p.flydrop2p.HandlerFactory
import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.data.local.AppDataStore
import com.flydrop2p.flydrop2p.data.local.AppDatabase
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.data.repository.ChatLocalRepository
import com.flydrop2p.flydrop2p.data.repository.ContactLocalRepository
import com.flydrop2p.flydrop2p.data.repository.OwnAccountLocalRepository
import com.flydrop2p.flydrop2p.data.repository.OwnProfileLocalRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import com.flydrop2p.flydrop2p.network.CallManager
import com.flydrop2p.flydrop2p.network.NetworkManager
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver

interface AppContainer {
    val context: Context
    val handlerFactory: HandlerFactory
    val wiFiDirectBroadcastReceiver: WiFiDirectBroadcastReceiver
    val chatRepository: ChatRepository
    val contactRepository: ContactRepository
    val ownAccountRepository: OwnAccountRepository
    val ownProfileRepository: OwnProfileRepository
    val fileManager: FileManager
    val networkManager: NetworkManager
    val callManager: CallManager
}

class AppDataContainer(activity: MainActivity) : AppContainer {
    override val context: Context = activity

    override val handlerFactory = HandlerFactory(context)

    override val wiFiDirectBroadcastReceiver = WiFiDirectBroadcastReceiver(context)

    override val chatRepository: ChatRepository by lazy {
        ChatLocalRepository(AppDatabase.getDatabase(context).contactDao(), AppDatabase.getDatabase(context).messageDao(), AppDatabase.getDatabase(context).profileDao())
    }

    override val contactRepository: ContactRepository by lazy {
        ContactLocalRepository(AppDatabase.getDatabase(context).contactDao(), AppDatabase.getDatabase(context).profileDao())
    }

    override val ownAccountRepository: OwnAccountRepository by lazy {
        OwnAccountLocalRepository(AppDataStore.getAccountRepository(context))
    }

    override val ownProfileRepository: OwnProfileRepository by lazy {
        OwnProfileLocalRepository(AppDataStore.getProfileRepository(context))
    }

    override val fileManager: FileManager by lazy {
        FileManager(context)
    }

    override val networkManager: NetworkManager by lazy {
        NetworkManager(ownAccountRepository, ownProfileRepository, handlerFactory, wiFiDirectBroadcastReceiver, chatRepository, contactRepository, fileManager)
    }

    override val callManager: CallManager by lazy {
        CallManager(context)
    }
}
