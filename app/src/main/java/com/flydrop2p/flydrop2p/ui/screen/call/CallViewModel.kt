package com.flydrop2p.flydrop2p.ui.screen.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.network.CallManager
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class CallViewModel(
    private val contactRepository: ContactRepository,
    private val callManager: CallManager,
    val networkManager: NetworkManager,
    private val accountId: Long
) : ViewModel() {
    private val _uiState = MutableStateFlow(CallViewState())
    val uiState: StateFlow<CallViewState> = _uiState.asStateFlow()

    private val isCalling = AtomicBoolean(false)

    init {
        viewModelScope.launch {
            contactRepository.getContactByAccountIdAsFlow(accountId).collect { contact ->
                if (contact != null) {
                    _uiState.value = _uiState.value.copy(contact = contact)
                }
            }
        }

        viewModelScope.launch {
            networkManager.callFragment.collect { audioBytes ->
                audioBytes?.let {
                    callManager.playAudio(audioBytes)
                }
            }
        }

        startCall(accountId)
    }

    fun sendCallEnd(accountId: Long) {
        networkManager.sendCallEnd(accountId, false)
    }

    private fun startCall(accountId: Long) {
        if(!isCalling.get()) {
            isCalling.set(true)

            callManager.startPlaying()
            callManager.startRecording { audioBytes ->
                networkManager.sendCallFragment(accountId, audioBytes)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun endCall() {
        if(isCalling.get()) {
            isCalling.set(false)

            GlobalScope.launch {
                callManager.stopPlaying()
                callManager.stopRecording()
            }
        }
    }

    fun setSpeakerOn(isSpeakerOn: Boolean) {
        _uiState.value = _uiState.value.copy(isSpeakerOn = isSpeakerOn)
    }
}