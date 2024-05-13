package com.flydrop2p.flydrop2p.network

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.ServerSocket

class ServerService {
    private val socket = ServerSocket(8888)

    suspend fun startConnection() {
        withContext(Dispatchers.IO) {
            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            val address = InetAddress.getByAddress(buffer)

            Log.d("ServerService", address.hostAddress ?: "")

            socket.close()
        }
    }
}
