package com.flydrop2p.flydrop2p.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    private val socket = Socket()

    suspend fun connectToServer() {
        withContext(Dispatchers.IO) {
            try {
                 // Create a client socket with the host, port, and timeout information.
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName("192.168.49.1"), 8888)))

                // Send the socket IP address to the server.
                val outputStream = socket.getOutputStream()
                outputStream.write(socket.localAddress.address)
                outputStream.close()
            } catch (e: Exception) {
                Log.d("ClientService", e.toString())
            }
        }
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                val outputStream = socket.getOutputStream()
                outputStream.write(message.toByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }
}