package com.flydrop2p.flydrop2p.ui.screen.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import com.flydrop2p.flydrop2p.network.BackupInstance
import com.flydrop2p.flydrop2p.network.BackupRequestBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository

class SettingsViewModel(
    private val ownAccountRepository: OwnAccountRepository,
    private val ownProfileRepository: OwnProfileRepository,
    private val fileManager: FileManager,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsViewState())
    val uiState: StateFlow<SettingsViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            ownProfileRepository.getProfileAsFlow().collect {
                _uiState.value = SettingsViewState(it)
            }
        }
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            ownProfileRepository.setUsername(username)
            ownAccountRepository.setProfileUpdate(System.currentTimeMillis())
        }
    }

    fun updateProfileImage(profileImageUri: Uri) {
        viewModelScope.launch {
            fileManager.saveProfileImage(profileImageUri, ownAccountRepository.getAccount().accountId)?.let { profileImageName ->
                ownProfileRepository.setImageFileName(profileImageName)
                ownAccountRepository.setProfileUpdate(System.currentTimeMillis())
            }
        }
    }

    fun backupMessages() {
        viewModelScope.launch {
            try {
                val messages = chatRepository.getAllMessagesByReceiverAccountId(uiState.value.profile.accountId)

                val response = BackupInstance.api.backupMessages(BackupRequestBody(uiState.value.profile.accountId, messages))

            } catch (e: Exception) {
                // Gestisci gli errori
            }
        }
    }


    fun retrieveBackup() {
        viewModelScope.launch {
            try {
                val messages = BackupInstance.api.getBackup(uiState.value.profile.accountId)
                messages.forEach { message ->
                    val existingMessage = chatRepository.getMessageByMessageId(message.messageId)

                    if (existingMessage == null) {
                        chatRepository.addMessage(message)
                    }
                }

            } catch (e: Exception) {
                // Gestisci gli errori
            }
        }
    }
}
