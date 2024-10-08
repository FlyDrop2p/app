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
    private val accountId: Long,
    callState: CallState
) : ViewModel() {
    private val _uiState = MutableStateFlow(CallViewState(callState))
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
    }

    fun sendCallEnd() {
        networkManager.sendCallEnd(accountId)
    }

    fun acceptCall() {
        networkManager.resetCallStateFlows()
        networkManager.sendCallResponse(accountId, true)
    }

    fun declineCall() {
        networkManager.resetCallStateFlows()
        networkManager.sendCallResponse(accountId, false)
    }

    fun startCall() {
        networkManager.resetCallStateFlows()
        startCallSession()
    }

    fun endCall() {
        networkManager.resetCallStateFlows()
        endCallSession()
    }

    private fun startCallSession() {
        if(!isCalling.get()) {
            try {
                callManager.startPlaying()
                callManager.startRecording { audioBytes ->
                    networkManager.sendCallFragment(accountId, audioBytes)
                }

                _uiState.value = _uiState.value.copy(callState = CallState.CALL)
                isCalling.set(true)
            } catch (_: Exception) {

            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun endCallSession() {
        if(isCalling.get()) {
            GlobalScope.launch {
                callManager.stopPlaying()
                callManager.stopRecording()
                isCalling.set(false)
            }
        }
    }

    fun setSpeakerOn() {
        _uiState.value = _uiState.value.copy(isSpeakerOn = true)
        callManager.enableSpeakerVolume()
    }

    fun setSpeakerOff() {
        _uiState.value = _uiState.value.copy(isSpeakerOn = false)
        callManager.disableSpeakerVolume()
    }
}