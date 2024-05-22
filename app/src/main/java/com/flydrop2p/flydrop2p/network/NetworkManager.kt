package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.MainActivity
import com.flydrop2p.flydrop2p.network.services.ClientService
import com.flydrop2p.flydrop2p.network.services.ServerService
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
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

    fun sendKeepalive() {
        receiver.requestGroupInfo {
            if (it.isGroupOwner) {
                coroutineScope.launch {
                    for (device in connectedDevices) {
                        device.ipAddress?.let {
                            clientService.sendKeepaliveGuest(it, connectedDevices + thisDevice)
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    clientService.sendKeepaliveOwner(thisDevice)
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
                connectedDevices.add(device)
                connectedDevices.remove(thisDevice)
            }
        }
    }

    private fun startKeepaliveGuestConnection() {
        coroutineScope.launch {
            while (true) {
                val devices = serverService.listenKeepaliveGuest()
                connectedDevices.addAll(devices)
                connectedDevices.remove(thisDevice)
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