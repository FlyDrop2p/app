package com.flydrop2p.flydrop2p.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val chatRepository: ChatRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            chatRepository.getAllChatPreviewsAsFlow().collect {
                _uiState.value = _uiState.value.copy(chatPreviews = it)
            }
        }

        viewModelScope.launch {
            networkManager.connectedDevices.collect { device ->
                _uiState.value = _uiState.value.copy(onlineChats = device.map { it.account.accountId }.toSet())
            }
        }
    }
}
