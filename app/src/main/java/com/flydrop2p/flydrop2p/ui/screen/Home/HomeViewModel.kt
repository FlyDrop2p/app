package com.flydrop2p.flydrop2p.ui.screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.toChatInfo
import com.flydrop2p.flydrop2p.domain.repository.ChatsInfoRepository
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
                chatsInfoRepository.getChatsInfo().collect { chatsInfoEntity ->
                    val chatsInfo = chatsInfoEntity.map { it.toChatInfo() }
                    _chatsInfoState.value = _chatsInfoState.value.copy(chats = chatsInfo)
                }
            } catch (e: Exception) {
                // Gestisci eventuali errori
            }
        }
    }

    fun getChatName(chatId: Int): String {
        return _chatsInfoState.value.chats.find { it.id == chatId }?.name ?: "Unknown"
    }

    // TODO: Only for testing purposes
    fun populateDatabase() {
        viewModelScope.launch {
            try {
                chatsInfoRepository.populateDatabase()
                // Aggiorna lo stato della UI per riflettere il popolamento del database
            } catch (e: Exception) {
                // Gestisci eventuali errori
            }
        }
    }
}