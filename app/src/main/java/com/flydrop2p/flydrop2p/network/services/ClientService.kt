package com.flydrop2p.flydrop2p.network.services

import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    suspend fun sendKeepaliveOwner(device: Device) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(IP_GROUP_OWNER), ServerService.PORT_KEEPALIVE_OWNER)))

                device.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(device).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendKeepaliveGuest(addressIp: String, devices: Set<Device>) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_KEEPALIVE_GUEST)))

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(devices).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendContentString(addressIp: String, device: Device, content: String) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_CONTENT_STRING)))

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(Pair(device, content)).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }
}