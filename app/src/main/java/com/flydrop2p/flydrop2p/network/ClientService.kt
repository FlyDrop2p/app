package com.flydrop2p.flydrop2p.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    suspend fun connectToServer() {
        for(port in ServerService.FIRST_SOCKET_PORT..ServerService.LAST_SOCKET_PORT) {
            try {
                tryServerSocket(port)
                return
            } catch (e: Exception) {
                Log.d("ClientService", e.toString())
            }
        }
    }

    private suspend fun tryServerSocket(port: Int) {
        withContext(Dispatchers.IO) {
            try {
                // Create a client socket with the host, port, and timeout information.
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName("192.168.49.1"), port)))

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