package com.flydrop2p.flydrop2p.ui.screen.Chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.data.local.MessageEntity
import com.flydrop2p.flydrop2p.domain.model.ChatInfo
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatsInfoRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(private val chatRepository: ChatRepository,
                    private val contactRepository: ContactRepository,
                    private val chatsInfoRepository: ChatsInfoRepository,
    ): ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    init {
        Log.d("ChatViewModel", "ChatViewModel created")
    }

    fun getChat(chatId: Int) {
        viewModelScope.launch {
            try {
                val chatMessages = chatRepository.getChatMessages(chatId).first().toMutableList()
                val chatName = chatsInfoRepository.getChatInfo(chatId).first().name
                val chatInfo = ChatInfo(chatId, chatName)
                _uiState.value = _uiState.value.copy(chatInfo = chatInfo, messageList = chatMessages)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error getting chat", e)
            }
        }
    }

    suspend fun getSenderName(senderId: Int): String {
        if (senderId == 0) {
            return "Me"
        }
        return withContext(Dispatchers.IO){
            val contact = contactRepository.getContact(senderId).first()
            contact?.name ?: "Unknown"
        }
    }


    fun addMessage(message: MessageEntity) {
        viewModelScope.launch {
            try {
                chatRepository.addChatMessage(message)
                val messageList = _uiState.value.messageList
                messageList.add(message)
                _uiState.value = _uiState.value.copy(messageList = messageList)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error adding message", e)
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