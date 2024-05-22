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

    fun startKeepaliveOwnerConnection() {
        coroutineScope.launch {
            while (true) {
                val newDevice = serverService.listenKeepaliveOwner()
                connectedDevices.add(newDevice)
                connectedDevices.remove(thisDevice)
            }
        }
    }

    fun startKeepaliveGuestConnection() {
        coroutineScope.launch {
            while (true) {
                val newDevices = serverService.listenKeepaliveGuest()
                connectedDevices.addAll(newDevices)
                connectedDevices.remove(thisDevice)
            }
        }
    }

    fun sendKeepalive() {
        receiver.requestGroupInfo {
            if (it.isGroupOwner) {
                coroutineScope.launch {
                    for (device in connectedDevices) {
                        device.ipAddress?.let {
                            clientService.sendKeepaliveToGuest(it, connectedDevices + thisDevice)
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
}