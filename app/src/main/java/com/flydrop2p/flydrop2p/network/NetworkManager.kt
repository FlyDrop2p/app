package com.flydrop2p.flydrop2p.network

import android.util.Log
import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.domain.model.Account
import com.flydrop2p.flydrop2p.network.services.ClientService
import com.flydrop2p.flydrop2p.network.services.ServerService
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class NetworkManager(activity: MainActivity) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver: WiFiDirectBroadcastReceiver = WiFiDirectBroadcastReceiver(activity)

    val thisDevice = Device(IP_GROUP_OWNER, Account())
    private val _connectedDevices = MutableStateFlow<Set<Device>>(setOf())
    val connectedDevices: StateFlow<Set<Device>> = _connectedDevices

    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        Log.d("DEVICE ID", thisDevice.accountId.toString())
    }

    fun sendKeepalive() {
        receiver.requestGroupInfo {
            if (it == null || it.isGroupOwner) {
                coroutineScope.launch {
                    for (device in connectedDevices.value) {
                        device.ipAddress?.let {
                            clientService.sendKeepaliveToGuest(it, connectedDevices.value + thisDevice)
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    clientService.sendKeepaliveToOwner(thisDevice)
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
                val device = serverService.listenKeepaliveOwner()

                if(device != thisDevice) {
                    _connectedDevices.value += device
                }
            }
        }
    }

    private fun startKeepaliveGuestConnection() {
        coroutineScope.launch {
            while (true) {
                val devices = serverService.listenKeepaliveGuest()
                _connectedDevices.value += devices.filter { it != thisDevice }
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
}