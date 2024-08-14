package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.domain.model.Profile
import com.flydrop2p.flydrop2p.domain.repository.AccountRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatInfoRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import com.flydrop2p.flydrop2p.network.service.ClientService
import com.flydrop2p.flydrop2p.network.service.ServerService
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch


class NetworkManager(
    activity: MainActivity,
    accountRepository: AccountRepository,
    profileRepository: ProfileRepository,
    private val chatInfoRepository: ChatInfoRepository,
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver: WiFiDirectBroadcastReceiver = WiFiDirectBroadcastReceiver(activity)

    private var thisDevice: Device? = null
    private val _connectedDevices = MutableStateFlow<List<Device>>(listOf())
    val connectedDevices: StateFlow<List<Device>> = _connectedDevices

    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        coroutineScope.launch {
            val account = accountRepository.account.first()
            val profile = profileRepository.profile.first()

            thisDevice = Device(null, account.accountId, profile)

            profileRepository.profile.collect {
                thisDevice?.profile = it
            }

            accountRepository.account.collect {
                thisDevice?.accountId = it.accountId
            }
        }
    }

    fun sendKeepalive() {
        thisDevice?.let { thisDevice ->
            coroutineScope.launch {
                clientService.sendKeepaliveToOwner(thisDevice)

                for (device in connectedDevices.value) {
                    if(device.ipAddress == null) {
                        device.ipAddress = IP_GROUP_OWNER
                    }

                    device.ipAddress?.let { ipAddress ->
                        clientService.sendKeepaliveToGuest(ipAddress, connectedDevices.value + thisDevice)
                    }
                }
            }
        }
    }

    fun startConnections() {
        startKeepaliveOwnerConnection()
        startKeepaliveGuestConnection()
        startContentStringConnection()
    }

    private fun startKeepaliveOwnerConnection() {
        coroutineScope.launch {
            while (true) {
                val ownerKeepalive = serverService.listenKeepaliveAsOwner()

                if(ownerKeepalive.device.accountId != thisDevice?.accountId) {
                    handleDeviceKeepalive(ownerKeepalive.device)
                }
            }
        }
    }

    private fun startKeepaliveGuestConnection() {
        coroutineScope.launch {
            while (true) {
                val guestKeepalive = serverService.listenKeepaliveAsGuest()

                for (device in guestKeepalive.devices) {
                    if(device.accountId != thisDevice?.accountId) {
                        handleDeviceKeepalive(device)
                    }
                }
            }
        }
    }

    private fun startContentStringConnection() {
        coroutineScope.launch {
            while (true) {
                val (device, content) = serverService.listenContentString()
                // TODO (device sent content to thisDevice)
            }
        }
    }

    private fun handleDeviceKeepalive(device: Device) {
        coroutineScope.launch {
            val contact = contactRepository.getContactById(device.accountId)

            if(contact == null) {
                chatRepository.addSingleChat(device)
            } else {
                chatRepository.updateSingleChat(contact)
            }

            _connectedDevices.value = (_connectedDevices.value.filter { it.accountId != device.accountId } + device)
        }
    }
}