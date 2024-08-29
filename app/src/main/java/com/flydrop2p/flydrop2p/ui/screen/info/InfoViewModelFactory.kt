package com.flydrop2p.flydrop2p.ui.screen.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class InfoViewModelFactory(private val accountId: Long) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
            val application = (extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.flydrop2p.flydrop2p.App)

            return InfoViewModel(
                application.container.chatRepository,
                application.container.contactRepository,
                application.container.networkManager,
                accountId
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}