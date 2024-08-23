package com.flydrop2p.flydrop2p.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val ownAccountRepository: OwnAccountRepository,
    private val ownProfileRepository: OwnProfileRepository
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

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            ownProfileRepository.setUsername(newUsername)
            ownAccountRepository.setProfileUpdate(System.currentTimeMillis() / 1000)
        }
    }

    fun updateProfileImage(newImageFileName: String) {
        viewModelScope.launch {
            ownProfileRepository.setImageFileName(newImageFileName)
            ownAccountRepository.setProfileUpdate(System.currentTimeMillis() / 1000)
        }
    }
}
