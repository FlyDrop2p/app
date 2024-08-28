package com.flydrop2p.flydrop2p.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class SettingsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            val application = (extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.flydrop2p.flydrop2p.App)

            return SettingsViewModel(
                application.container.ownAccountRepository,
                application.container.ownProfileRepository,
                application.container.chatRepository,
                application.container.fileManager,
                application.container.networkManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}