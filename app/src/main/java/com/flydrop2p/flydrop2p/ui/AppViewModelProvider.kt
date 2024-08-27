package com.flydrop2p.flydrop2p.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.flydrop2p.flydrop2p.ui.screen.call.CallViewModel
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModel
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                application().container.chatRepository,
                application().container.contactRepository,
                application().container.networkManager
            )
        }

        initializer {
            ChatViewModel(
                application().container.chatRepository,
                application().container.contactRepository,
                application().container.ownAccountRepository,
                application().container.fileManager,
                application().container.networkManager,
            )
        }

        initializer {
            CallViewModel(
                application().container.handlerFactory,
                application().container.contactRepository,
                application().container.ownAccountRepository,
                application().container.networkManager,
                application().container.callManager
            )
        }

        initializer {
            SettingsViewModel(
                application().container.ownAccountRepository,
                application().container.ownProfileRepository,
                application().container.fileManager,
                application().container.chatRepository
            )
        }
    }
}

fun CreationExtras.application(): com.flydrop2p.flydrop2p.App =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.flydrop2p.flydrop2p.App)
