package com.flydrop2p.flydrop2p.ui.screen.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallViewModel(
    private val contactRepository: ContactRepository,
    private val ownAccountRepository: OwnAccountRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(CallViewState())
    val uiState: StateFlow<CallViewState> = _uiState.asStateFlow()

    val ownAccount
        get() = ownAccountRepository.getAccountAsFlow()

    fun collectContact(accountId: Long) {
        viewModelScope.launch {
            contactRepository.getContactByAccountIdAsFlow(accountId).collect { contact ->
                if (contact != null) {
                    _uiState.value = _uiState.value.copy(contact = contact)
                }
            }
        }
    }

    fun setSpeakerOn(isSpeakerOn: Boolean) {
        _uiState.value = _uiState.value.copy(isSpeakerOn = isSpeakerOn)
    }

}