package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NetworkManager(activity: MainActivity) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver = WiFiDirectBroadcastReceiver(activity)

    private val device: Device = Device((0..Long.MAX_VALUE).random(), null)
    private val devices = mutableSetOf<Device>()
    private val serverService = ServerService()
    private val clientService = ClientService()

    fun startServerConnection() {
        serverService.isHandshakeSocketOpen.onEach {
            if (!it) {
                coroutineScope.launch {
                    val device = serverService.startConnection()
                    devices.add(device)
                }
            }
        }
    }

    fun connectToServer() {
        clientService.connectToServer(device)
    }
}