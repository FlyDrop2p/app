package com.example.flydrop2p.ui.screen.Chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flydrop2p.data.DataSource
import com.example.flydrop2p.domain.model.Chat
import com.example.flydrop2p.domain.model.Message
import com.example.flydrop2p.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository): ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    init {
        Log.d("!!ChatViewModel!!", "ChatViewModel created")
    }

    fun setChat(chatId: Int) {
        viewModelScope.launch {
            try {
                val chat = chatRepository.getChat(chatId)
                _uiState.value = _uiState.value.copy(chat = chat)
                Log.d("ChatViewModel", "Chat: $chat")
                Log.d("ChatViewModel STATE", "${_uiState.value.chat}")
            } catch (e: Exception) {

            }
        }
    }

    fun getSenderName(senderId: Int): String {
        return DataSource.getSenderNameById(senderId)
    }

}