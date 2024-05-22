package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.network.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    suspend fun connectToServer(device: Device) {
        withContext(Dispatchers.IO) {
            try {
                // Create a client socket with the host, port, and timeout information.
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(IP_GROUP_OWNER), ServerService.PORT_HANDSHAKE)))
                device.ipAddress = socket.localAddress.hostAddress?.toString()

                // Send device info to server.
                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(device).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendKeepalive(addressIp: String, devices: Set<Device>) {
        withContext(Dispatchers.IO) {
            try {
                // Create a client socket with the host, port, and timeout information.
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_KEEPALIVE)))

                // Send device info to server.
                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(devices).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                val outputStream = socket.getOutputStream()
                outputStream.write(message.toByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }
}