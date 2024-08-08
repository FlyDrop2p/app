package com.flydrop2p.flydrop2p.network

import android.util.Log
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

    init {
        Log.d("DEVICE ID", thisDevice.id.toString())
    }

    fun sendKeepalive() {
        receiver.requestGroupInfo {
            if (it != null) {
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
                Log.d("KEEPALIVE", device.toString())
                connectedDevices.add(device)
                connectedDevices.remove(thisDevice)
            }
        }
    }

    private fun startKeepaliveGuestConnection() {
        coroutineScope.launch {
            while (true) {
                val devices = serverService.listenKeepaliveGuest()
                for(device in devices) {
                    Log.d("KEEPALIVE", device.toString())
                }
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