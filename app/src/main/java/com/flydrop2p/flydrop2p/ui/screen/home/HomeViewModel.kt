package com.flydrop2p.flydrop2p.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.NetworkManager
import com.flydrop2p.flydrop2p.network.toContact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val contactRepository: ContactRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            contactRepository.getAllContacts().collect {
                _uiState.value = _uiState.value.copy(contacts = it)
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
