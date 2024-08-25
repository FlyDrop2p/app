package com.flydrop2p.flydrop2p.ui.screen.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val ownAccountRepository: OwnAccountRepository,
    private val ownProfileRepository: OwnProfileRepository,
    private val fileManager: FileManager
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
}
