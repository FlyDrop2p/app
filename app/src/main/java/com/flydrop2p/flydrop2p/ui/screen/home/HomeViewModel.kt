package com.flydrop2p.flydrop2p.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.model.toChatInfo
import com.flydrop2p.flydrop2p.domain.model.toChatInfoEntity
import com.flydrop2p.flydrop2p.domain.repository.ChatInfoRepository
import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(
    private val chatInfoRepository: ChatInfoRepository,
    private val networkManager: NetworkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkManager.connectedDevices.collect { devices ->
                updateChatsBasedOnConnectedDevices(devices)
            }
        }
    }

    private suspend fun updateChatsBasedOnConnectedDevices(devices: Set<Device>) { // TODO check logic
        val chats = mutableSetOf<ChatInfo>()

        for (device in devices) {
            try {
                val chatInfos = chatInfoRepository.getChatInfosByContactId(device.contactId)
                chats.addAll(chatInfos)
            } catch (e: Exception) {
                // Log dell'eccezione per debugging
                e.printStackTrace()
            }
        }
        _uiState.value = _uiState.value.copy(chatList = chats.toList())
    }

//    private fun loadChatsInfo() {
//        viewModelScope.launch {
//            try {
//                chatInfoRepository.getAllChatInfos().collect { chatsInfoEntity ->
//                    val chatsInfo = chatsInfoEntity.map { it.toChatInfo() }
//                    _uiState.value = _uiState.value.copy(chatList = chatsInfo)
//                }
//            } catch (e: Exception) {
//                // Log dell'eccezione per debugging
//                e.printStackTrace()
//            }
//        }
//    }

    fun getChatName(chatId: Int): String {
        return _uiState.value.chatList.find { it.chatId == chatId }?.name ?: "Unknown"
    }

    // TODO: Only for testing purposes
    fun populateDatabase() {
        viewModelScope.launch {
            try {
                chatInfoRepository.populateDatabase()
                // Aggiorna lo stato della UI per riflettere il popolamento del database
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
