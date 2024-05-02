package com.example.flydrop2p.ui.screen.Chat

import androidx.lifecycle.ViewModel
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    fun setChatInfo(chatInfo: ChatInfo) {
        _uiState.value = ChatViewState(chatInfo = chatInfo)
    }

    fun setNewMessage(newMessage: String) {
        _uiState.value = _uiState.value.copy(newMessage = newMessage)
    }

    fun clearNewMessage() {
        _uiState.value = _uiState.value.copy(newMessage = "")
    }

    fun addMessage(message: Message) {
        val chatInfo = _uiState.value.chatInfo
        val newMessages = chatInfo.messages.toMutableList()
        newMessages.add(message)
        _uiState.value = _uiState.value.copy(chatInfo = chatInfo.copy(messages = newMessages))
    }

    fun clearChat() {
        _uiState.value = ChatViewState()
    }

    fun updateChatInfo(chatInfo: ChatInfo) {
        _uiState.value = _uiState.value.copy(chatInfo = chatInfo)
    }

}