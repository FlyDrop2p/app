package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.network.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NetworkManager(activity: MainActivity) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val receiver = WiFiDirectBroadcastReceiver(activity)

    private val thisDevice = Device(android.os.Build.VERSION.SDK_INT.toLong(), IP_GROUP_OWNER)
    private val connectedDevices = mutableSetOf<Device>()
    private val serverService = ServerService()
    private val clientService = ClientService()

    fun startHandshakeConnection() {
        coroutineScope.launch {
            while(true) {
                val newDevice = serverService.startHandshakeConnection()
                connectedDevices.add(newDevice)
                connectedDevices.remove(thisDevice)
            }
        }
    }

    fun startKeepaliveConnection() {
        coroutineScope.launch {
            while(true) {
                val newDevice = serverService.startKeepaliveConnection()
                connectedDevices.add(newDevice)
                connectedDevices.remove(thisDevice)
            }
        }
    }

    fun connectToServer() {
        receiver.requestGroupInfo {
            if(!it.isGroupOwner) {
                coroutineScope.launch {
                    clientService.connectToServer(thisDevice)
                }
            }
        }
    }

    fun sendKeepalive() {
        receiver.requestGroupInfo {
            if (it.isGroupOwner) {
                coroutineScope.launch {
                    for (device in connectedDevices) {
                        device.ipAddress?.let {
                            clientService.sendKeepalive(it, thisDevice)
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    clientService.sendKeepalive(IP_GROUP_OWNER, thisDevice)
                }
            }
        }
    }
}