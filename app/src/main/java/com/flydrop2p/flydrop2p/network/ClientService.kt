package com.flydrop2p.flydrop2p.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    fun connectToServer(device: Device) {
        try {
            // Create a client socket with the host, port, and timeout information.
            val socket = Socket()
            socket.bind(null)
            socket.connect((InetSocketAddress(InetAddress.getByName("192.168.49.1"), ServerService.PORT_HANDSHAKE)))
            device.ipAddress = socket.inetAddress.hostAddress?.toString()

            // Send device info to server.
            val outputStream = socket.getOutputStream()
            outputStream.write(Json.encodeToString(device).toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            throw e
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