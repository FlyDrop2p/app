package com.flydrop2p.flydrop2p.ui.screen.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.model.message.Message
import com.flydrop2p.flydrop2p.domain.model.message.MessageState
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.media.AudioReplayer
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    private val ownAccountRepository: OwnAccountRepository,
    private val fileManager: FileManager,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatViewState())
    val uiState: StateFlow<ChatViewState> = _uiState.asStateFlow()

    private val audioReplayer = AudioReplayer()

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
            chatRepository.getAllMessagesByAccountIdAsFlow(accountId).collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun sendTextMessage(accountId: Long, text: String) {
        networkManager.sendTextMessage(accountId, text)
    }

    fun sendFileMessage(accountId: Long, fileUri: Uri) {
        networkManager.sendFileMessage(accountId, fileUri)
    }

    fun sendAudioMessage(accountId: Long) {
        audioReplayer.apply {
            if(isRecording) {
                stopRecording()
                recordingFile?.let { networkManager.sendAudioMessage(accountId, it) }
            }
        }
    }

    fun startRecordingAudio() = audioReplayer.startRecording(fileManager.getAudioTempFile())
    fun stopRecordingAudio() = audioReplayer.stopRecording()
    fun startPlayingAudio(file: File) = audioReplayer.startPlaying(file)
    fun stopPlayingAudio() = audioReplayer.stopPlaying()

    fun updateMessagesState(messages: List<Message>) {
        viewModelScope.launch {
            val ownAccountId = ownAccountRepository.getAccount().accountId

            val receivedMessages = messages.filter { it.messageState < MessageState.MESSAGE_READ && it.senderId != ownAccountId }

            receivedMessages.forEach { message ->
                chatRepository.updateMessageState(message.messageId, MessageState.MESSAGE_READ)
            }

            receivedMessages.lastOrNull()?.let { message ->
                networkManager.sendMessageReadAck(message.senderId, message.messageId)
            }
        }
    }

    fun resetChat() {
        _uiState.value = ChatViewState()
    }
}