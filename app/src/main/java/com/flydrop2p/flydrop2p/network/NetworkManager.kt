package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.model.contact.Account
import com.flydrop2p.flydrop2p.domain.model.contact.Contact
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toAccount
import com.flydrop2p.flydrop2p.domain.model.contact.toNetworkProfile
import com.flydrop2p.flydrop2p.domain.model.contact.toProfile
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.domain.model.message.toNetworkFileMessage
import com.flydrop2p.flydrop2p.domain.model.message.toNetworkTextMessage
import com.flydrop2p.flydrop2p.domain.model.message.toTextMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkDevice
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage
import com.flydrop2p.flydrop2p.network.model.message.NetworkTextMessage
import com.flydrop2p.flydrop2p.network.model.profile.NetworkProfileRequest
import com.flydrop2p.flydrop2p.network.model.profile.NetworkProfileResponse
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
import java.io.File


class NetworkManager(
    activity: MainActivity,
    ownAccountRepository: OwnAccountRepository,
    ownProfileRepository: OwnProfileRepository,
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    private val fileManager: FileManager
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver: WiFiDirectBroadcastReceiver = WiFiDirectBroadcastReceiver(activity)

    private var thisDevice = Device(null, Contact(Account(0, 0), null))

    private val _connectedDevices: MutableStateFlow<List<Device>> = MutableStateFlow(listOf())
    val connectedDevices: StateFlow<List<Device>>
        get() = _connectedDevices

    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        coroutineScope.launch {
            ownAccountRepository.getAccountAsFlow().collect {
                thisDevice = thisDevice.copy(contact = thisDevice.contact.copy(account = it))
            }
        }

        coroutineScope.launch {
            ownProfileRepository.getProfileAsFlow().collect {
                thisDevice = thisDevice.copy(contact = thisDevice.contact.copy(profile = it))
            }
        }
    }

    fun sendKeepalive() {
        coroutineScope.launch {
            val networkKeepalive = NetworkKeepalive(connectedDevices.value.map { it.toNetworkDevice() } + thisDevice.toNetworkDevice())
            clientService.sendKeepalive(IP_GROUP_OWNER, thisDevice, networkKeepalive)

            for (device in connectedDevices.value) {
                device.ipAddress?.let {
                    clientService.sendKeepalive(it, thisDevice, networkKeepalive)
                }
            }
        }
    }

    fun sendProfileRequest(accountId: Int) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val networkProfileRequest = NetworkProfileRequest(thisDevice.account.accountId, accountId)
                    clientService.sendProfileRequest(it, thisDevice, networkProfileRequest)
                }
            }
        }
    }

    fun sendProfileResponse(accountId: Int) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val networkProfileResponse = NetworkProfileResponse(thisDevice.account.accountId, accountId, thisDevice.profile?.toNetworkProfile()!!)
                    clientService.sendProfileResponse(it, thisDevice, networkProfileResponse)
                }
            }
        }
    }

    fun sendTextMessage(accountId: Int, text: String) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val textMessage = TextMessage(thisDevice.account.accountId, accountId, text, System.currentTimeMillis() / 1000)
                    chatRepository.addChatMessage(textMessage)
                    clientService.sendTextMessage(it, thisDevice, textMessage.toNetworkTextMessage())
                }
            }
        }
    }

    fun sendFileMessage(accountId: Int, file: File) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val fileMessage = FileMessage(thisDevice.account.accountId, accountId, file, System.currentTimeMillis() / 1000)
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
        startProfileRequestConnection()
        startProfileResponseConnection()
    }

    private fun startKeepaliveConnection() {
        coroutineScope.launch {
            while(true) {
                val networkKeepalive = serverService.listenKeepalive()

                for(networkDevice in networkKeepalive.networkDevices) {
                    if(networkDevice.account.accountId != thisDevice.account.accountId) {
                        handleDeviceKeepalive(networkDevice)
                    }
                }
            }
        }
    }

    private fun startProfileRequestConnection() {
        coroutineScope.launch {
            while(true) {
                val networkProfileRequest = serverService.listenProfileRequest()

                if(networkProfileRequest.receiverId == thisDevice.account.accountId) {
                    handleProfileRequest(networkProfileRequest)
                }
            }
        }
    }

    private fun startProfileResponseConnection() {
        coroutineScope.launch {
            while(true) {
                val networkProfileResponse = serverService.listenProfileResponse()

                if(networkProfileResponse.receiverId == thisDevice.account.accountId) {
                    handleProfileResponse(networkProfileResponse)
                }
            }
        }
    }

    private fun startTextMessageConnection() {
        coroutineScope.launch {
            while(true) {
                val networkTextMessage = serverService.listenTextMessage()

                if(networkTextMessage.receiverId == thisDevice.account.accountId) {
                    handleTextMessage(networkTextMessage)
                }
            }
        }
    }
    
    private fun startFileMessageConnection() {
        coroutineScope.launch { 
            while(true) {
                val networkFileMessage = serverService.listenFileMessage()

                if(networkFileMessage.receiverId == thisDevice.account.accountId) {
                    handleFileMessage(networkFileMessage)
                }
            }
        }
    }

    private fun handleDeviceKeepalive(networkDevice: NetworkDevice) {
        coroutineScope.launch {
            val lastAccount = contactRepository.getAccountByAccountId(networkDevice.account.accountId)
            contactRepository.addOrUpdateAccount(networkDevice.account.toAccount())

            val profile = contactRepository.getProfileByAccountId(networkDevice.account.accountId)

            if(profile == null || (lastAccount != null && lastAccount.profileUpdate < networkDevice.account.profileUpdate)) {
                sendProfileRequest(networkDevice.account.accountId)
            }

            _connectedDevices.value = _connectedDevices.value.filter { it.account.accountId != networkDevice.account.accountId } + Device(networkDevice, profile)
        }
    }

    private fun handleProfileRequest(networkProfileRequest: NetworkProfileRequest) {
        coroutineScope.launch {
            sendProfileResponse(networkProfileRequest.senderId)
        }
    }

    private fun handleProfileResponse(networkProfileResponse: NetworkProfileResponse) {
        coroutineScope.launch {
            contactRepository.addOrUpdateProfile(networkProfileResponse.profile.toProfile())

            _connectedDevices.value = _connectedDevices.value.map { device ->
                if(device.account.accountId != thisDevice.account.accountId) {
                    device
                } else {
                    device.copy(contact = device.contact.copy(profile = networkProfileResponse.profile.toProfile()))
                }
            }
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