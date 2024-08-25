package com.flydrop2p.flydrop2p.network.service

import android.util.Log
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage
import com.flydrop2p.flydrop2p.network.model.message.NetworkMessageAck
import com.flydrop2p.flydrop2p.network.model.message.NetworkTextMessage
import com.flydrop2p.flydrop2p.network.model.profile.NetworkProfileRequest
import com.flydrop2p.flydrop2p.network.model.profile.NetworkProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.ServerSocket

class ServerService {
    companion object {
        const val PORT_KEEPALIVE: Int = 8800
        const val PORT_PROFILE_REQUEST: Int = 8801
        const val PORT_PROFILE_RESPONSE: Int = 8802
        const val PORT_TEXT_MESSAGE: Int = 8803
        const val PORT_FILE_MESSAGE: Int = 8804
        const val PORT_MESSAGE_RECEIVED_ACK = 8805
        const val PORT_MESSAGE_READ_ACK = 8806
    }

    suspend fun listenKeepalive(): NetworkKeepalive {
        val networkKeepalive: NetworkKeepalive

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkKeepalive = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("KEEPALIVE", networkKeepalive.toString())
        }

        return networkKeepalive
    }

    suspend fun listenProfileRequest(): NetworkProfileRequest {
        val networkProfileRequest: NetworkProfileRequest

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_PROFILE_REQUEST)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkProfileRequest = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("PROFILE REQUEST", networkProfileRequest.toString())
        }

        return networkProfileRequest
    }

    suspend fun listenProfileResponse(): NetworkProfileResponse {
        val networkProfileResponse: NetworkProfileResponse

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_PROFILE_RESPONSE)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkProfileResponse = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("PROFILE RESPONSE", networkProfileResponse.toString())
        }

        return networkProfileResponse
    }

    suspend fun listenTextMessage(): NetworkTextMessage {
        val networkTextMessage: NetworkTextMessage

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_TEXT_MESSAGE)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkTextMessage = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("TEXT MESSAGE", networkTextMessage.toString())
        }

        return networkTextMessage
    }

    suspend fun listenFileMessage(): NetworkFileMessage {
        val networkFileMessage: NetworkFileMessage

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_FILE_MESSAGE)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkFileMessage = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("FILE MESSAGE", networkFileMessage.toString())
        }

        return networkFileMessage
    }

    suspend fun listenMessageReceivedAck(): NetworkMessageAck {
        val networkMessageAck: NetworkMessageAck

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_MESSAGE_RECEIVED_ACK)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkMessageAck = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("MESSAGE RECEIVED ACK", networkMessageAck.toString())
        }

        return networkMessageAck
    }

    suspend fun listenMessageReadAck(): NetworkMessageAck {
        val networkMessageAck: NetworkMessageAck

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_MESSAGE_READ_ACK)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            networkMessageAck = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("MESSAGE READ ACK", networkMessageAck.toString())
        }

        return networkMessageAck
    }
}
