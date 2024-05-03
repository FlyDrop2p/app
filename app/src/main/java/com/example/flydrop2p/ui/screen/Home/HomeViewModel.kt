package com.example.flydrop2p.ui.screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flydrop2p.domain.repository.ChatsInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val chatsInfoRepository: ChatsInfoRepository) : ViewModel() {

    private val _chatsInfoState = MutableStateFlow(HomeViewState())
    val chatsInfoState: StateFlow<HomeViewState> = _chatsInfoState

    init {
        loadChatsInfo()
    }

    private fun loadChatsInfo() {
        viewModelScope.launch {
            try {
                val chatsInfo = chatsInfoRepository.getChatsInfo()
                _chatsInfoState.value = _chatsInfoState.value.copy(chats = chatsInfo)
            } catch (e: Exception) {
                // Gestisci eventuali errori
            }
        }
    }

}