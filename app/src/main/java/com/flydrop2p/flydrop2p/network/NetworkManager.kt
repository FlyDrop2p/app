package com.flydrop2p.flydrop2p.network

import android.util.Log
import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.network.services.ClientService
import com.flydrop2p.flydrop2p.network.services.ServerService
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random


object NetworkManager {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    lateinit var receiver: WiFiDirectBroadcastReceiver

    private val thisDevice = Device(Random.nextLong(), IP_GROUP_OWNER)
    private val _connectedDevices = MutableStateFlow<Set<Device>>(emptySet())
    val connectedDevices: StateFlow<Set<Device>> = _connectedDevices.asStateFlow()
    private val serverService = ServerService()
    private val clientService = ClientService()

    init {
        Log.d("DEVICE ID", thisDevice.id.toString())
    }

    fun init(activity: MainActivity) {
        receiver = WiFiDirectBroadcastReceiver(activity)
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
                val currentDevices = _connectedDevices.value.toMutableSet()
                currentDevices.add(device)
                currentDevices.remove(thisDevice)
                _connectedDevices.value = currentDevices.toSet()
            }
        }
    }

    private fun startKeepaliveGuestConnection() {
        coroutineScope.launch {
            while (true) {
                val devices = serverService.listenKeepaliveGuest()
                val currentDevices = _connectedDevices.value.toMutableSet()
                currentDevices.addAll(devices)
                currentDevices.remove(thisDevice)
                _connectedDevices.value = currentDevices.toSet()
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