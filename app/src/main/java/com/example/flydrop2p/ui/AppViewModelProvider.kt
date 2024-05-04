package com.example.flydrop2p.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flydrop2p.FlydropApplication
import com.example.flydrop2p.MainActivity
import com.example.flydrop2p.ui.screen.Chat.ChatViewModel
import com.example.flydrop2p.ui.screen.Home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                // this.createSavedStateHandle()
                flydropApplication().container.chatsInfoRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ChatViewModel(
                flydropApplication().container.chatRepository,
                flydropApplication().container.contactRepository
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlydropApplication].
 */
fun CreationExtras.flydropApplication(): FlydropApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlydropApplication)
