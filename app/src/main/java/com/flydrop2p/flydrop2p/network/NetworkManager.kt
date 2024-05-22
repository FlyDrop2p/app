package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.network.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NetworkManager(activity: MainActivity) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver = WiFiDirectBroadcastReceiver(activity)

    private val thisDevice = Device((0..Long.MAX_VALUE).random(), null)
    private val connectedDevices = mutableSetOf<Device>()
    val serverService = ServerService()
    private val clientService = ClientService()

    fun startHandshakeConnection() {
        coroutineScope.launch {
            val newDevice = serverService.startHandshakeConnection()
            connectedDevices.add(newDevice)
            connectedDevices.remove(thisDevice)
        }

        serverService.isHandshakeSocketOpen.onEach {
            if (!it) {
                coroutineScope.launch {
                    val newDevice = serverService.startHandshakeConnection()
                    connectedDevices.add(newDevice)
                    connectedDevices.remove(thisDevice)
                }
            }
        }
    }

    fun startKeepaliveConnection() {
        serverService.isKeepaliveSocketOpen.onEach {
            if (!it) {
                coroutineScope.launch {
                    val newDevices = serverService.startKeepaliveConnection()
                    connectedDevices.addAll(newDevices)
                    connectedDevices.remove(thisDevice)
                }
            }
        }
    }

    fun connectToServer() {
        if (!receiver.isGroupOwner()) {
            coroutineScope.launch {
                clientService.connectToServer(thisDevice)
            }
        }
    }

    fun sendKeepalive() {
        if (receiver.isGroupOwner()) {
            coroutineScope.launch {
                for (device in connectedDevices) {
                    device.ipAddress?.let {
                        clientService.sendKeepalive(it, connectedDevices + thisDevice)

                    }
                }
            }
        } else {
            coroutineScope.launch {
                clientService.sendKeepalive(IP_GROUP_OWNER, connectedDevices + thisDevice)
            }
        }
    }
}