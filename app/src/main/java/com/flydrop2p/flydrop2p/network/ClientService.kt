package com.flydrop2p.flydrop2p.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun connectToServer() {
        coroutineScope.launch {
            try {
                // Create a client socket with the host, port, and timeout information.
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName("192.168.49.1"), ServerService.PORT_HANDSHAKE)))

                // Send the socket IP address to the server.
                val outputStream = socket.getOutputStream()
                outputStream.write(socket.localAddress.address)
                outputStream.close()
            } catch (e: Exception) {
                throw e
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