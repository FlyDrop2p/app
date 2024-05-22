package com.flydrop2p.flydrop2p.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.ServerSocket

class ServerService {
    companion object {
        const val PORT_KEEPALIVE_OWNER: Int = 8800
        const val PORT_KEEPALIVE_GUEST: Int = 8890
    }

    suspend fun listenKeepaliveOwner(): Device {
        val device: Device

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE_OWNER)

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            device = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("OWNER KEEPALIVE", device.toString())
        }

        return device
    }

    suspend fun listenKeepaliveGuest(): Set<Device> {
        val devices: Set<Device>

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE_GUEST)

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            devices = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("GUEST KEEPALIVE", devices.toString())
        }

        return devices
    }
}
