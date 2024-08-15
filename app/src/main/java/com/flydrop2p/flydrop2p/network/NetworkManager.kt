package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.repository.AccountRepository
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
import kotlinx.coroutines.launch


class NetworkManager(
    activity: MainActivity,
    accountRepository: AccountRepository,
    profileRepository: ProfileRepository,
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver: WiFiDirectBroadcastReceiver = WiFiDirectBroadcastReceiver(activity)

    private lateinit var thisDevice: Device
    private val _connectedDevices = MutableStateFlow<List<Device>>(listOf())
    val connectedDevices: StateFlow<List<Device>> = _connectedDevices

    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        coroutineScope.launch {
            val account = accountRepository.account.first()
            val profile = profileRepository.profile.first()

            thisDevice = Device(null, Contact(account.accountId, profile))

            profileRepository.setUsername(profile.username)
            accountRepository.setAccountId(account.accountId)

            profileRepository.profile.collect {
                thisDevice = thisDevice.copy(contact = thisDevice.contact.copy(profile = it))
            }

            accountRepository.account.collect {
                thisDevice = thisDevice.copy(contact = thisDevice.contact.copy(accountId = it.accountId))
            }
        }
    }

    fun sendKeepalive() {
        coroutineScope.launch {
            clientService.sendKeepalive(IP_GROUP_OWNER, thisDevice, connectedDevices.value)

            for (device in connectedDevices.value) {
                device.ipAddress?.let {
                    clientService.sendKeepalive(it, thisDevice, connectedDevices.value)
                }
            }
        }
    }

    fun sendMessage() {

    }

    fun startConnections() {
        startKeepaliveConnection()
        startContentStringConnection()
    }

    private fun startKeepaliveConnection() {
        coroutineScope.launch {
            while (true) {
                val keepalive = serverService.listenKeepalive()

                for(device in keepalive.devices) {
                    if(device.accountId != thisDevice.accountId) {
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
                contactRepository.addContact(device.contact)
            } else {
                contactRepository.updateContact(device.contact)
            }

            _connectedDevices.value = (_connectedDevices.value.filter { it.accountId != device.accountId } + device)
        }
    }
}