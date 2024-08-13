package com.flydrop2p.flydrop2p.ui.screen.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.Profile
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
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().collect { profile ->
                Log.d("SettingsViewModel", "loadProfile: $profile")
                _uiState.value = SettingsViewState(profile)
            }
        }
    }

    fun updateUsername(newUsername: String) {
        Log.d("SettingsViewModel", "updateUsername: $newUsername")
        viewModelScope.launch {
            profileRepository.setUsername(newUsername)
        }
    }
}
