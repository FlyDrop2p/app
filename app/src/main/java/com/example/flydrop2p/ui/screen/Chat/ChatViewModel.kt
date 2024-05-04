package com.example.flydrop2p.ui.screen.Chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flydrop2p.data.local.MessageEntity
import com.example.flydrop2p.data.repository.LocalChatInfoRepository
import com.example.flydrop2p.domain.model.Chat
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.domain.model.Message
import com.example.flydrop2p.domain.model.toMessage
import com.example.flydrop2p.domain.model.toMessageEntity
import com.example.flydrop2p.domain.repository.ChatRepository
import com.example.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatViewModel(private val chatRepository: ChatRepository,
                    private val contactRepository: ContactRepository
    ): ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    fun setChat(chatInfo: ChatInfo) {
        viewModelScope.launch {
            try {
                val messagesEntity = chatRepository.getChatMessages(chatInfo.id)

                val messagesList = messagesEntity.single()

                val messages = messagesList.map { it.toMessage() }

                val chat = Chat(chatInfo.id, chatInfo.name, messages)

                _uiState.value = _uiState.value.copy(chat = chat)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error getting chat messages", e)
            }
        }
    }

    fun getSenderName(senderId: Int): String {
        return runBlocking {
            try {
                val contact = contactRepository.getContact(senderId).singleOrNull()
                val senderName = contact?.name ?: "Unknown sender"
                senderName
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error getting contact", e)
                "Unknown sender"
            }
        }
    }


    fun addMessage(message: Message) {
        viewModelScope.launch {
            try {
                chatRepository.addChatMessage(message.toMessageEntity())
                val chat = _uiState.value.chat
                val updatedMessages = chat.messages + message
                val updatedChat = chat.copy(messages = updatedMessages)
                _uiState.value = _uiState.value.copy(chat = updatedChat)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error adding message", e)
            }
        }
    }

    fun populateDatabase() {
        viewModelScope.launch {
            chatRepository.populateDatabase()
            contactRepository.populateDatabase()
        }
    }
}