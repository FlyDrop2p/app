package com.flydrop2p.flydrop2p.ui.screen.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    private val ownAccountRepository: OwnAccountRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    val ownAccount
        get() = ownAccountRepository.getAccountAsFlow()

    fun collectContact(accountId: Long) {
        viewModelScope.launch {
            contactRepository.getContactByAccountIdAsFlow(accountId).collect {
                it?.let {
                    _uiState.value = _uiState.value.copy(contact = it)
                }
            }
        }
    }

    fun collectMessages(accountId: Long) {
        viewModelScope.launch {
            chatRepository.getMessagesByAccountId(accountId).collect {
                // TODO: sign message as read
                // chatRepository.signMessagesAsRead(accountId) // from accountId to me
                _uiState.value = _uiState.value.copy(messages = it)
            }
        }
    }

    fun sendTextMessage(receiverId: Long, text: String) {
        viewModelScope.launch {
            networkManager.sendTextMessage(receiverId, text)
        }
    }

    fun sendFileMessage(receiverId: Long, fileUri: Uri) {
        viewModelScope.launch {
            networkManager.sendFileMessage(receiverId, fileUri)
        }
    }

    fun sendMessageReadAck(receiverId: Long, messageId: Long) {
        viewModelScope.launch {
            networkManager.sendMessageReadAck(receiverId, messageId)
        }
    }

    fun resetChat() {
        _uiState.value = ChatViewState()
    }
}