package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.domain.model.contact.Profile
import com.flydrop2p.flydrop2p.domain.model.contact.toAccount
import com.flydrop2p.flydrop2p.domain.model.message.FileMessage
import com.flydrop2p.flydrop2p.domain.model.message.TextMessage
import com.flydrop2p.flydrop2p.domain.model.message.toNetworkTextMessage
import com.flydrop2p.flydrop2p.domain.model.message.toTextMessage
import com.flydrop2p.flydrop2p.domain.repository.ChatRepository
import com.flydrop2p.flydrop2p.domain.repository.ContactRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnAccountRepository
import com.flydrop2p.flydrop2p.domain.repository.OwnProfileRepository
import com.flydrop2p.flydrop2p.network.model.contact.NetworkProfile
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

    private lateinit var ownDevice: Device

    private val _connectedDevices: MutableStateFlow<List<NetworkDevice>> = MutableStateFlow(listOf())
    val connectedDevices: StateFlow<List<NetworkDevice>>
        get() = _connectedDevices

    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        coroutineScope.launch {
            ownDevice = Device(null, ownAccountRepository.getAccount(), ownProfileRepository.getProfile())
        }

        coroutineScope.launch {
            ownAccountRepository.getAccountAsFlow().collect {
                ownDevice = ownDevice.copy(account = it)
            }
        }

        coroutineScope.launch {
            ownProfileRepository.getProfileAsFlow().collect {
                ownDevice = ownDevice.copy(profile = it)
            }
        }
    }

    fun sendKeepalive() {
        coroutineScope.launch {
            val networkKeepalive = NetworkKeepalive(connectedDevices.value + ownDevice.toNetworkDevice())
            clientService.sendKeepalive(IP_GROUP_OWNER, ownDevice, networkKeepalive)

            for (device in connectedDevices.value) {
                device.ipAddress?.let {
                    clientService.sendKeepalive(it, ownDevice, networkKeepalive)
                }
            }
        }
    }

    fun sendProfileRequest(accountId: Long) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val networkProfileRequest = NetworkProfileRequest(ownDevice.account.accountId, accountId)
                    clientService.sendProfileRequest(it, ownDevice, networkProfileRequest)
                }
            }
        }
    }

    fun sendProfileResponse(accountId: Long) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val image = ownDevice.profile.imageFileName?.let { fileName ->
                        fileManager.loadFile(fileName)
                    }

                    val networkProfile = NetworkProfile(ownDevice.profile, image)
                    val networkProfileResponse = NetworkProfileResponse(ownDevice.account.accountId, accountId, networkProfile)
                    clientService.sendProfileResponse(it, ownDevice, networkProfileResponse)
                }
            }
        }
    }

    fun sendTextMessage(accountId: Long, text: String) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
                    val textMessage = TextMessage(0, ownDevice.account.accountId, accountId, System.currentTimeMillis(), text, false)
                    textMessage.messageId = chatRepository.addChatMessage(textMessage)
                    clientService.sendTextMessage(it, ownDevice, textMessage.toNetworkTextMessage())
                }
            }
        }
    }

    fun sendFileMessage(accountId: Long, file: File) {
        val connectedDevice = connectedDevices.value.find { it.account.accountId == accountId }

        connectedDevice?.let { device ->
            coroutineScope.launch {
                device.ipAddress?.let {
//                    val fileMessage = FileMessage(0, ownDevice.account.accountId, accountId, System.currentTimeMillis(), file, false)
//                    chatRepository.addChatMessage(fileMessage)
//                    clientService.sendFileMessage(it, ownDevice, fileMessage.toNetworkFileMessage())
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
                    if(networkDevice.account.accountId != ownDevice.account.accountId) {
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

                if(networkProfileRequest.receiverId == ownDevice.account.accountId) {
                    handleProfileRequest(networkProfileRequest)
                }
            }
        }
    }

    private fun startProfileResponseConnection() {
        coroutineScope.launch {
            while(true) {
                val networkProfileResponse = serverService.listenProfileResponse()

                if(networkProfileResponse.receiverId == ownDevice.account.accountId) {
                    handleProfileResponse(networkProfileResponse)
                }
            }
        }
    }

    private fun startTextMessageConnection() {
        coroutineScope.launch {
            while(true) {
                val networkTextMessage = serverService.listenTextMessage()

                if(networkTextMessage.receiverId == ownDevice.account.accountId) {
                    handleTextMessage(networkTextMessage)
                }
            }
        }
    }
    
    private fun startFileMessageConnection() {
        coroutineScope.launch { 
            while(true) {
                val networkFileMessage = serverService.listenFileMessage()

                if(networkFileMessage.receiverId == ownDevice.account.accountId) {
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

            _connectedDevices.value = _connectedDevices.value.filter { it.account.accountId != networkDevice.account.accountId } + networkDevice
        }
    }

    private fun handleProfileRequest(networkProfileRequest: NetworkProfileRequest) {
        coroutineScope.launch {
            sendProfileResponse(networkProfileRequest.senderId)
        }
    }

    private fun handleProfileResponse(networkProfileResponse: NetworkProfileResponse) {
        coroutineScope.launch {
            val imageFileName = networkProfileResponse.profile.image?.let {
                fileManager.saveProfileImage(it, networkProfileResponse.profile.accountId)
            }

            contactRepository.addOrUpdateProfile(Profile(networkProfileResponse.profile, imageFileName))
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
//            val fileMessage = networkFileMessage.toFileMessage()
//
//            val file = File(fileMessage.file.path)
//            file.writeBytes(networkFileMessage.file)
//
//            chatRepository.addChatMessage(fileMessage)
        }
    }
}