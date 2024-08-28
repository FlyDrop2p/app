package com.flydrop2p.flydrop2p.ui.screen.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.model.message.toMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import com.flydrop2p.flydrop2p.network.BackupInstance
import com.flydrop2p.flydrop2p.network.BackupRequestBody
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val ownAccountRepository: OwnAccountRepository,
    val ownProfileRepository: OwnProfileRepository,
    private val chatRepository: ChatRepository,
    private val fileManager: FileManager,
    val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsViewState())
    val uiState: StateFlow<SettingsViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            ownProfileRepository.getProfileAsFlow().collect {
                _uiState.value = SettingsViewState(profile = it)
            }
        }
    }

    fun connect() {
        networkManager.receiver.discoverPeers()
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun setSuccess(isSuccess: Boolean) {
        _uiState.value = _uiState.value.copy(isSuccess = isSuccess)
    }

    fun setError(message: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val currentTimestamp = System.currentTimeMillis()
                ownProfileRepository.setUsername(username)
                ownProfileRepository.setUpdateTimestamp(currentTimestamp)
                ownAccountRepository.setProfileUpdateTimestamp(currentTimestamp)
                setSuccess(true)
            } catch (e: Exception) {
                setError("Failed to update username")
            } finally {
                setLoading(false)
            }
        }
    }

    fun updateProfileImage(profileImageUri: Uri) {
        viewModelScope.launch {
            setLoading(true)
            try {
                fileManager.saveProfileImage(profileImageUri, ownAccountRepository.getAccount().accountId)?.let { profileImageName ->
                    val currentTimestamp = System.currentTimeMillis()
                    ownProfileRepository.setImageFileName(profileImageName)
                    ownProfileRepository.setUpdateTimestamp(currentTimestamp)
                    ownAccountRepository.setProfileUpdateTimestamp(currentTimestamp)
                    setSuccess(true)
                }
            } catch (e: Exception) {
                setError("Failed to update profile image")
            } finally {
                setLoading(false)
            }
        }
    }

    fun backupMessages() {
        viewModelScope.launch {
            setLoading(true)
            try {
                Log.d("Backup", "Backup started")
                val messages = chatRepository.getAllMessages().map { it.toMessageEntity() }
                val body = BackupRequestBody(uiState.value.profile.accountId, messages)
                val response = BackupInstance.api.backupMessages(body)
                Log.d("Backup", "Backup completed with message: ${response.message}")
                setSuccess(true)
            } catch (e: Exception) {
                setError("Failed to backup messages")
            } finally {
                setLoading(false)
            }
        }
    }

    fun retrieveBackup() {
        viewModelScope.launch {
            setLoading(true)
            try {
                val messages = BackupInstance.api.getBackup(uiState.value.profile.accountId)
                Log.d("Backup", "Backup retrieved")
                messages.forEach { message ->
                    val existingMessage = chatRepository.getMessageByMessageId(message.messageId)
                    if (existingMessage == null) {
                        chatRepository.addMessage(message.toMessage())
                    }
                }
                setSuccess(true)
            } catch (e: Exception) {
                setError("Failed to retrieve backup")
            } finally {
                setLoading(false)
            }
        }
    }
}
