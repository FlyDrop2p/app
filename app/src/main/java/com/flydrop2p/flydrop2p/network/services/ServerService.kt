package com.flydrop2p.flydrop2p.network.services

import android.util.Log
import com.flydrop2p.flydrop2p.network.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.ServerSocket

class ServerService {
    companion object {
        const val PORT_KEEPALIVE_OWNER: Int = 8800
        const val PORT_KEEPALIVE_GUEST: Int = 8801
        const val PORT_CONTENT_STRING: Int = 8802
    }

    suspend fun listenKeepaliveOwner(): Device {
        val ret: Device

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE_OWNER)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            ret = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("OWNER KEEPALIVE", ret.toString())
        }

        return ret
    }

    suspend fun listenKeepaliveGuest(): Set<Device> {
        val ret: Set<Device>

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE_GUEST)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            ret = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("GUEST KEEPALIVE", ret.toString())
        }

        return ret
    }

    suspend fun listenContentString(): Pair<Device, String> {
        val ret: Pair<Device, String>

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_CONTENT_STRING)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            ret = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("CONTENT STRING", ret.toString())
        }

        return ret
    }
}
