package com.flydrop2p.flydrop2p.ui.screen.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flydrop2p.flydrop2p.HandlerFactory
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.network.CallManager
import com.flydrop2p.flydrop2p.network.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallViewModel(
    private val handlerFactory: HandlerFactory,
    private val contactRepository: ContactRepository,
    private val ownAccountRepository: OwnAccountRepository,
    private val networkManager: NetworkManager,
    private val callManager: CallManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(CallViewState())
    val uiState: StateFlow<CallViewState> = _uiState.asStateFlow()

    val ownAccount
        get() = ownAccountRepository.getAccountAsFlow()

    init {
        viewModelScope.launch {
            networkManager.callFragmentFile.collect { file ->
                file?.let {
                    callManager.playAudio(it)
                }
            }
        }
    }

    fun collectContact(accountId: Long) {
        viewModelScope.launch {
            contactRepository.getContactByAccountIdAsFlow(accountId).collect { contact ->
                if (contact != null) {
                    _uiState.value = _uiState.value.copy(contact = contact)
                }
            }
        }
    }

    fun startCall(accountId: Long) {
        callManager.startRecording()
        startCallFragmentHandler(accountId)
    }

    fun endCall() {
        callManager.stopRecording()
        callManager.deleteRecordingFiles()
    }

    private fun startCallFragmentHandler(accountId: Long) {
        val handler = handlerFactory.buildHandler()

        val runnable = object : Runnable {
            override fun run() {
                callManager.sampleAudioRecording()?.let { file ->
                    networkManager.sendCallFragment(accountId, file)
                    file.delete()
                }
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)
    }

    fun setSpeakerOn(isSpeakerOn: Boolean) {
        _uiState.value = _uiState.value.copy(isSpeakerOn = isSpeakerOn)
    }
}