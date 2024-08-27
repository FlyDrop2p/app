package com.flydrop2p.flydrop2p.ui.screen.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.message.toMessage
import com.flydrop2p.flydrop2p.media.FileManager
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
            val currentTimestamp = System.currentTimeMillis()
            ownProfileRepository.setUsername(username)
            ownProfileRepository.setUpdateTimestamp(currentTimestamp)
            ownAccountRepository.setProfileUpdateTimestamp(currentTimestamp)
        }
    }

    fun updateProfileImage(profileImageUri: Uri) {
        viewModelScope.launch {
            fileManager.saveProfileImage(profileImageUri, ownAccountRepository.getAccount().accountId)?.let { profileImageName ->
                val currentTimestamp = System.currentTimeMillis()
                ownProfileRepository.setImageFileName(profileImageName)
                ownProfileRepository.setUpdateTimestamp(currentTimestamp)
                ownAccountRepository.setProfileUpdateTimestamp(currentTimestamp)
            }
        }
    }

    fun backupMessages() {
        viewModelScope.launch {
            try {
                Log.d("Backup", "Backup started")
                val messages = chatRepository.getAllMessages().map { it.toMessageEntity() }

                val temp = 5

                val body = BackupRequestBody(uiState.value.profile.accountId, messages)

                Log.d("Backup", body.toString())

                val response = BackupInstance.api.backupMessages(body)

                Log.d("Backup", "Backup completed with message: ${response.message}")
            } catch (e: Exception) {
                // Gestisci gli errori
            }
        }
    }


    fun retrieveBackup() {
        viewModelScope.launch {
            try {
                val messages = BackupInstance.api.getBackup(uiState.value.profile.accountId)
                Log.d("Backup", "Backup retrieved")

                messages.forEach { message ->
                    val existingMessage = chatRepository.getMessageByMessageId(message.messageId)

                    if (existingMessage == null) {
                        chatRepository.addMessage(message.toMessage())
                    }
                }

            } catch (e: Exception) {
                // Gestisci gli errori
            }
        }
    }
}
