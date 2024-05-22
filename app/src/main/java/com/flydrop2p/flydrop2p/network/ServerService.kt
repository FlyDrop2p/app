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
    val _isHandshakeSocketOpen = MutableStateFlow(false)
    val isHandshakeSocketOpen: StateFlow<Boolean>
        get() = _isHandshakeSocketOpen.asStateFlow()

    val _isKeepaliveSocketOpen = MutableStateFlow(false)
    val isKeepaliveSocketOpen: StateFlow<Boolean>
        get() = _isKeepaliveSocketOpen.asStateFlow()

    companion object {
        const val PORT_HANDSHAKE: Int = 8800
        const val PORT_KEEPALIVE: Int = 8890
    }

    suspend fun startHandshakeConnection(): Device {
        val device: Device

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_HANDSHAKE)
            _isHandshakeSocketOpen.value = true

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            device = Json.decodeFromString(buffer.decodeToString())

            socket.close()
            _isHandshakeSocketOpen.value = false

            Log.d("HANDSHAKE", device.toString())
        }

        return device
    }

    suspend fun startKeepaliveConnection(): Set<Device> {
        val devices: Set<Device>

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE)
            _isKeepaliveSocketOpen.value = true

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            devices = Json.decodeFromString(buffer.decodeToString())

            socket.close()
            _isKeepaliveSocketOpen.value = false

            Log.d("KEEPALIVE", devices.toString())
        }

        return devices
    }
}
