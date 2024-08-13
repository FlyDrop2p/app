package com.flydrop2p.flydrop2p.ui.screen.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.model.Message
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatInfoRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.network.NetworkManager
import com.flydrop2p.flydrop2p.network.services.ClientService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    private val chatInfoRepository: ChatInfoRepository,
    private val networkManager: NetworkManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    init {
        Log.d("ChatViewModel", "ChatViewModel created")
    }

    fun getChat(chatId: Int) {
        viewModelScope.launch {
            try {
                val chatMessages = chatRepository.getChatMessagesByChatId(chatId)
                val chatInfo = chatInfoRepository.getChatInfoById(chatId)!!
                _uiState.value = _uiState.value.copy(chatInfo = chatInfo, messageList = chatMessages.last().toMutableList())
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error getting chat", e)
            }
        }
    }

    suspend fun getSenderName(senderId: Int): String {
        if (senderId == 0) {
            return "Me"
        }
        return withContext(Dispatchers.IO) {
            val contact = contactRepository.getContactById(senderId)
            contact?.username ?: "Unknown"
        }
    }


    fun addMessage(message: Message) {
        viewModelScope.launch {
            try {
                val updatedMessageList = _uiState.value.messageList.toMutableList().apply {
                    add(message)
                }
                _uiState.value = _uiState.value.copy(messageList = updatedMessageList)
                chatRepository.addChatMessage(message)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error adding message", e)
            }
        }
    }

    fun sendMessage(receiverIp: String, message: String) {
        viewModelScope.launch {
            try {
                val clientService = ClientService()
                clientService.sendContentString(receiverIp, networkManager.thisDevice, message)
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