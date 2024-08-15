package com.flydrop2p.flydrop2p.ui.screen.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    fun collectContact(accountId: Int) {
        viewModelScope.launch {
            contactRepository.getContactById(accountId)?.collect {
                _uiState.value = _uiState.value.copy(contact = it)
            }
        }
    }

    fun collectMessages(accountId: Int) {
        viewModelScope.launch {
            chatRepository.getChatMessagesByAccountId(accountId).collect {
                _uiState.value = _uiState.value.copy(messages = it)
            }
        }
    }

    fun addMessage(message: Message) {
        viewModelScope.launch {
            try {
                val updatedMessageList = _uiState.value.messages.toMutableList().apply {
                    add(message)
                }
                _uiState.value = _uiState.value.copy(messages = updatedMessageList)
                chatRepository.addChatMessage(message)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error adding message", e)
            }
        }
    }

    fun sendMessage(receiverIp: String, message: String) {
        viewModelScope.launch {
            try {
                // networkManager.sendContentString(receiverIp, networkManager.thisDevice, message) TODO
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message", e)
            }
        }
    }


    fun resetChat() {
        _uiState.value = ChatViewState()
    }

    fun populateDatabase() {
        viewModelScope.launch {
            chatRepository.populateDatabase()
            contactRepository.populateDatabase()
        }
    }
}