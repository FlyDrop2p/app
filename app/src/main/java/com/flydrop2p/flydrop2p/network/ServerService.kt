package com.flydrop2p.flydrop2p.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket

class ServerService {
    private val serverSocket = ServerSocket(8888)
    suspend fun createConnection() {
        withContext(Dispatchers.IO) {
            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = serverSocket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes().decodeToString()
        }
    }
}
