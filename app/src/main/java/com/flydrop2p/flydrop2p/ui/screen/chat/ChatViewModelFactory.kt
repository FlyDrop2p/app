package com.flydrop2p.flydrop2p.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class ChatViewModelFactory(private val accountId: Long) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            val application = (extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.flydrop2p.flydrop2p.App)

            return ChatViewModel(
                application.container.chatRepository,
                application.container.contactRepository,
                application.container.ownAccountRepository,
                application.container.fileManager,
                application.container.networkManager,
                accountId
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}