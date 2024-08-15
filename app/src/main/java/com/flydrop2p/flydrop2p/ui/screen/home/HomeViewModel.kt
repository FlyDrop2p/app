package com.flydrop2p.flydrop2p.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val chatRepository: ChatRepository, private val contactRepository: ContactRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            chatRepository.getAllChatPreviews().collect {
                _uiState.value = _uiState.value.copy(chatPreviews = it)
            }
        }
    }

    // TODO: Only for testing purposes
    fun populateDatabase() {
//        viewModelScope.launch {
//            try {
//                loadContacts()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }
}
