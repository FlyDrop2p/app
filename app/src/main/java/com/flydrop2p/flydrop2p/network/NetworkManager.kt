package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.domain.model.Contact
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.domain.model.message.toNetworkFileMessage
import com.flydrop2p.flydrop2p.domain.model.message.toNetworkTextMessage
import com.flydrop2p.flydrop2p.domain.model.message.toTextMessage
import com.flydrop2p.flydrop2p.domain.repository.AccountRepository
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.ProfileRepository
import com.flydrop2p.flydrop2p.network.model.NetworkFileMessage
import com.flydrop2p.flydrop2p.network.model.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.NetworkTextMessage
import com.flydrop2p.flydrop2p.network.service.ClientService
import com.flydrop2p.flydrop2p.network.service.ServerService
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File


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
            val networkKeepalive = NetworkKeepalive(connectedDevices.value + thisDevice)
            clientService.sendKeepalive(IP_GROUP_OWNER, thisDevice, networkKeepalive)

            for (device in connectedDevices.value) {
                device.ipAddress?.let {
                    clientService.sendKeepalive(it, thisDevice, networkKeepalive)
                }
            }
        }
    }

    fun sendTextMessage(accountId: Int, text: String) {
        val connectedDevice = connectedDevices.value.find { it.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val textMessage = TextMessage(thisDevice.accountId, accountId, text, System.currentTimeMillis() / 1000)
                    chatRepository.addChatMessage(textMessage)
                    clientService.sendTextMessage(it, thisDevice, textMessage.toNetworkTextMessage())
                }
            }
        }
    }

    fun sendFileMessage(accountId: Int, file: File) {
        val connectedDevice = connectedDevices.value.find { it.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val fileMessage = FileMessage(thisDevice.accountId, accountId, file, System.currentTimeMillis() / 1000)
                    chatRepository.addChatMessage(fileMessage)
                    clientService.sendFileMessage(it, thisDevice, fileMessage.toNetworkFileMessage())
                }
            }
        }
    }

    fun startConnections() {
        startKeepaliveConnection()
        startTextMessageConnection()
        startFileMessageConnection()
    }

    private fun startKeepaliveConnection() {
        coroutineScope.launch {
            while(true) {
                val networkKeepalive = serverService.listenKeepalive()

                for(device in networkKeepalive.devices) {
                    if(device.accountId != thisDevice.accountId) {
                        handleDeviceKeepalive(device)
                    }
                }
            }
        }
    }

    private fun startTextMessageConnection() {
        coroutineScope.launch {
            while(true) {
                val networkTextMessage = serverService.listenTextMessage()

                if(networkTextMessage.receiverId == thisDevice.accountId) {
                    handleTextMessage(networkTextMessage)
                }
            }
        }
    }
    
    private fun startFileMessageConnection() {
        coroutineScope.launch { 
            while(true) {
                val networkFileMessage = serverService.listenFileMessage()

                if(networkFileMessage.receiverId == thisDevice.accountId) {
                    handleFileMessage(networkFileMessage)
                }
            }
        }
    }

    private fun handleDeviceKeepalive(device: Device) {
        coroutineScope.launch {
            val contact = contactRepository.getContactById(device.accountId)

            if(contact.firstOrNull() == null) {
                contactRepository.addContact(device.contact)
            } else {
                contactRepository.updateContact(device.contact)
            }

            _connectedDevices.value = (_connectedDevices.value.filter { it.accountId != device.accountId } + device)
        }
    }

    private fun handleTextMessage(networkTextMessage: NetworkTextMessage) {
        coroutineScope.launch {
            val textMessage = networkTextMessage.toTextMessage()
            chatRepository.addChatMessage(textMessage)
        }
    }
    
    private fun handleFileMessage(networkFileMessage: NetworkFileMessage) {
        coroutineScope.launch {
            // TODO
        }
    }
}