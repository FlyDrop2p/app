package com.flydrop2p.flydrop2p.network.service

import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.model.keepalive.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.message.NetworkFileMessage
import com.flydrop2p.flydrop2p.network.model.message.NetworkTextMessage
import com.flydrop2p.flydrop2p.network.model.profile.NetworkProfileRequest
import com.flydrop2p.flydrop2p.network.model.profile.NetworkProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    suspend fun sendKeepalive(ipAddress: String, thisDevice: Device, networkKeepalive: NetworkKeepalive) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(ipAddress), ServerService.PORT_KEEPALIVE)))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkKeepalive).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendProfileRequest(ipAddress: String, thisDevice: Device, networkProfileRequest: NetworkProfileRequest) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(ipAddress), ServerService.PORT_PROFILE_REQUEST)))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkProfileRequest).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendProfileResponse(ipAddress: String, thisDevice: Device, networkProfileRequest: NetworkProfileResponse) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(ipAddress), ServerService.PORT_PROFILE_RESPONSE)))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkProfileRequest).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendTextMessage(ipAddress: String, thisDevice: Device, networkTextMessage: NetworkTextMessage) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(ipAddress), ServerService.PORT_TEXT_MESSAGE)))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkTextMessage).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendFileMessage(ipAddress: String, thisDevice: Device, networkFileMessage: NetworkFileMessage) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect(InetSocketAddress(InetAddress.getByName(ipAddress), ServerService.PORT_FILE_MESSAGE))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkFileMessage).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }
}