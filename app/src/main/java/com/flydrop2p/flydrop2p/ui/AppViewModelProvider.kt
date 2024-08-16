package com.flydrop2p.flydrop2p.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModel
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                application().container.chatRepository,
                application().container.contactRepository
            )
        }

        initializer {
            ChatViewModel(
                application().container.chatRepository,
                application().container.contactRepository,
                application().container.accountRepository,
                application().container.networkManager,
            )
        }

        initializer {
            SettingsViewModel(
                application().container.profileRepository,
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [Application].
 */
fun CreationExtras.application(): com.flydrop2p.flydrop2p.App =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.flydrop2p.flydrop2p.App)
