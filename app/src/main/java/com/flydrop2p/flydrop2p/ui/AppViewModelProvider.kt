package com.flydrop2p.flydrop2p.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.flydrop2p.flydrop2p.FlyDropApplication
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModel
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                // this.createSavedStateHandle()
                flydropApplication().container.chatsInfoRepository,
                flydropApplication().container.networkManager
            )
        }

        initializer {
            ChatViewModel(
                flydropApplication().container.chatRepository,
                flydropApplication().container.contactRepository,
                flydropApplication().container.chatsInfoRepository,
                flydropApplication().container.networkManager
            )
        }

        initializer {
            SettingsViewModel(

            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlyDropApplication].
 */
fun CreationExtras.flydropApplication(): FlyDropApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlyDropApplication)
