package com.flydrop2p.flydrop2p.ui.screen.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
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
            contactRepository.getContactByAccountIdAsFlow(accountId).collect { contact ->
                if(contact != null) {
                    _uiState.value = _uiState.value.copy(contact = contact)
                }
            }
        }
    }

    fun collectMessages(accountId: Long) {
        viewModelScope.launch {
            chatRepository.getMessagesByAccountId(accountId).collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun sendTextMessage(accountId: Long, text: String) {
        viewModelScope.launch {
            networkManager.sendTextMessage(accountId, text)
        }
    }

    fun sendFileMessage(accountId: Long, fileUri: Uri) {
        viewModelScope.launch {
            networkManager.sendFileMessage(accountId, fileUri)
        }
    }

    fun sendMessageReadAck(accountId: Long, messageId: Long) {
        viewModelScope.launch {
            chatRepository.updateMessageState(messageId, MessageState.MESSAGE_READ)
            networkManager.sendMessageReadAck(accountId, messageId)
        }
    }

    fun resetChat() {
        _uiState.value = ChatViewState()
    }
}