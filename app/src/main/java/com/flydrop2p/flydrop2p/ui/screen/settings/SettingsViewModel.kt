package com.flydrop2p.flydrop2p.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsViewState())
    val uiState: StateFlow<SettingsViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect {
                _uiState.value = SettingsViewState(it)
            }
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            profileRepository.setUsername(newUsername)
        }
    }

//    fun updateProfileImage(newImagePath: String) {
//        viewModelScope.launch {
//            profileRepository.setImageFilePath(newImagePath)
//        }
//    }
}
