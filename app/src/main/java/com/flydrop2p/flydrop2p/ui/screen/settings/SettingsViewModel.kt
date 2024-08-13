package com.flydrop2p.flydrop2p.ui.screen.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.Profile
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsViewState())
    val uiState: StateFlow<SettingsViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.getProfile().collectLatest { profile ->
                Log.d("SettingsViewModel", "Profile updated: $profile")
                _uiState.value = SettingsViewState(profile)
            }
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            profileRepository.setUsername(newUsername)
            Log.d("SettingsViewModel", "Username updated to $newUsername") //OK
        }
    }
}
