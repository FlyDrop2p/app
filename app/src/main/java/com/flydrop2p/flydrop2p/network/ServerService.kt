package com.flydrop2p.flydrop2p.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.ServerSocket

class ServerService {
    private val _isHandshakeSocketOpen = MutableStateFlow(false)
    val isHandshakeSocketOpen: StateFlow<Boolean> get() = _isHandshakeSocketOpen.asStateFlow()

    companion object {
        const val PORT_HANDSHAKE: Int = 8800
    }

    suspend fun startConnection(): Device {
        val device: Device

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_HANDSHAKE)
            _isHandshakeSocketOpen.value = true

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            device = Json.decodeFromString(buffer.toString())

            socket.close()
            _isHandshakeSocketOpen.value = false

            Log.d("ServerService, Socket: $PORT_HANDSHAKE", device.toString())
        }

        return device
    }
}
