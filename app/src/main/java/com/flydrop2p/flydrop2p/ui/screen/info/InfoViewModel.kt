package com.flydrop2p.flydrop2p.ui.screen.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoViewModel(
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    val networkManager: NetworkManager,
    private val accountId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(InfoViewState())
    val uiState: StateFlow<InfoViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            contactRepository.getContactByAccountIdAsFlow(accountId).collect { contact ->
                if(contact?.profile != null) {
                    _uiState.value = contact.profile.let { _uiState.value.copy(profile = it) }
                }
            }
        }

        viewModelScope.launch {
            chatRepository.getAllMediaMessagesByAccountIdAsFlow(accountId).collect { messages ->
                val mediaMessages = messages.filterIsInstance<FileMessage>()
                _uiState.value = _uiState.value.copy(mediaMessages = mediaMessages)
            }
        }
    }
}