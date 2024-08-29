package com.flydrop2p.flydrop2p.network.service

import android.util.Log
import com.flydrop2p.flydrop2p.network.model.call.NetworkCallEnd
import com.flydrop2p.flydrop2p.network.model.call.NetworkCallRequest
import com.flydrop2p.flydrop2p.network.model.call.NetworkCallResponse
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.message.NetworkAudioMessage
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
        const val PORT_AUDIO_MESSAGE: Int = 8805
        const val PORT_MESSAGE_RECEIVED_ACK: Int = 8806
        const val PORT_MESSAGE_READ_ACK: Int = 8807
        const val PORT_CALL_REQUEST: Int = 8808
        const val PORT_CALL_RESPONSE: Int = 8009
        const val PORT_CALL_END: Int = 8810
        const val PORT_CALL_FRAGMENT: Int = 8811
    }

    suspend fun listenKeepalive(): NetworkKeepalive? {
        var networkKeepalive: NetworkKeepalive? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_KEEPALIVE)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkKeepalive = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("KEEPALIVE", networkKeepalive.toString())
            } catch (e: Exception) {
                Log.d("KEEPALIVE", e.toString())
            }
        }

        return networkKeepalive
    }

    suspend fun listenProfileRequest(): NetworkProfileRequest? {
        var networkProfileRequest: NetworkProfileRequest? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_PROFILE_REQUEST)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkProfileRequest = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("PROFILE REQUEST", networkProfileRequest.toString())
            } catch (e: Exception) {
                Log.d("PROFILE REQUEST", e.toString())
            }
        }

        return networkProfileRequest
    }

    suspend fun listenProfileResponse(): NetworkProfileResponse? {
        var networkProfileResponse: NetworkProfileResponse? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_PROFILE_RESPONSE)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkProfileResponse = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("PROFILE RESPONSE", networkProfileResponse.toString())
            } catch (e: Exception) {
                Log.d("PROFILE RESPONSE", e.toString())
            }
        }

        return networkProfileResponse
    }

    suspend fun listenTextMessage(): NetworkTextMessage? {
        var networkTextMessage: NetworkTextMessage? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_TEXT_MESSAGE)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkTextMessage = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("TEXT MESSAGE", networkTextMessage.toString())
            } catch (e: Exception) {
                Log.d("TEXT MESSAGE", e.toString())
            }
        }

        return networkTextMessage
    }

    suspend fun listenFileMessage(): NetworkFileMessage? {
        var networkFileMessage: NetworkFileMessage? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_FILE_MESSAGE)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkFileMessage = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("FILE MESSAGE", networkFileMessage.toString())
            } catch (e: Exception) {
                Log.d("FILE MESSAGE", e.toString())
            }
        }

        return networkFileMessage
    }

    suspend fun listenAudioMessage(): NetworkAudioMessage? {
        var networkAudioMessage: NetworkAudioMessage? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_AUDIO_MESSAGE)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkAudioMessage = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("AUDIO MESSAGE", networkAudioMessage.toString())
            } catch (e: Exception) {
                Log.d("AUDIO MESSAGE", e.toString())
            }
        }

        return networkAudioMessage
    }

    suspend fun listenMessageReceivedAck(): NetworkMessageAck? {
        var networkMessageAck: NetworkMessageAck? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_MESSAGE_RECEIVED_ACK)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkMessageAck = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("MESSAGE RECEIVED ACK", networkMessageAck.toString())
            } catch (e: Exception) {
                Log.d("MESSAGE RECEIVED ACK", e.toString())
            }
        }

        return networkMessageAck
    }

    suspend fun listenMessageReadAck(): NetworkMessageAck? {
        var networkMessageAck: NetworkMessageAck? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_MESSAGE_READ_ACK)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkMessageAck = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("MESSAGE READ ACK", networkMessageAck.toString())
            } catch (e: Exception) {
                Log.d("MESSAGE READ ACK", e.toString())
            }
        }

        return networkMessageAck
    }

    suspend fun listenCallRequest(): NetworkCallRequest? {
        var networkCallRequest: NetworkCallRequest? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_CALL_REQUEST)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkCallRequest = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("CALL REQUEST", networkCallRequest.toString())
            } catch (e: Exception) {
                Log.d("CALL REQUEST", e.toString())
            }
        }

        return networkCallRequest
    }

    suspend fun listenCallResponse(): NetworkCallResponse? {
        var networkCallResponse: NetworkCallResponse? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_CALL_RESPONSE)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkCallResponse = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("CALL RESPONSE", networkCallResponse.toString())
            } catch (e: Exception) {
                Log.d("CALL RESPONSE", e.toString())
            }
        }

        return networkCallResponse
    }

    suspend fun listenCallEnd(): NetworkCallEnd? {
        var networkCallRequest: NetworkCallEnd? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_CALL_END)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                networkCallRequest = Json.decodeFromString(buffer.decodeToString())

                socket.close()

                Log.d("CALL END", networkCallRequest.toString())
            } catch (e: Exception) {
                Log.d("CALL END", e.toString())
            }
        }

        return networkCallRequest
    }

    suspend fun listenCallFragment(): ByteArray? {
        var audio: ByteArray? = null

        withContext(Dispatchers.IO) {
            try {
                val socket = ServerSocket(PORT_CALL_FRAGMENT)
                val client = socket.accept()

                val inputStream = client.getInputStream()
                val buffer = inputStream.readBytes()
                audio = buffer

                socket.close()
            } catch (_: Exception) {

            }
        }

        return audio
    }
}
