package com.flydrop2p.flydrop2p.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.ServerSocket

class ServerService {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    companion object {
        const val FIRST_SOCKET_PORT: Int = 8800
        const val LAST_SOCKET_PORT: Int = 8804
    }

    fun startConnection() {
        for(port in FIRST_SOCKET_PORT..LAST_SOCKET_PORT) {
            coroutineScope.launch {
                openSocket(port)
            }
        }
    }

    private suspend fun openSocket(port: Int) {
        withContext(Dispatchers.IO) {
            val socket = ServerSocket(port)

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            val address = InetAddress.getByAddress(buffer)

            Log.d("ServerService, Socket: ${port}", address.hostAddress ?: "")

            socket.close()
        }
    }
}
