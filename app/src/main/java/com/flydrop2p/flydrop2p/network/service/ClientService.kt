package com.flydrop2p.flydrop2p.network.service

import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.model.NetworkFileMessage
import com.flydrop2p.flydrop2p.network.model.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.NetworkTextMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    suspend fun sendKeepalive(ipAddress: String, thisDevice: Device, connectedDevices: List<Device>) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(ipAddress), ServerService.PORT_KEEPALIVE)))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()
                val networkKeepalive = NetworkKeepalive(connectedDevices.toList() + thisDevice)

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkKeepalive).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendTextMessage(addressIp: String, thisDevice: Device, text: String) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_TEXT_MESSAGE)))

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()
                val networkTextMessage = NetworkTextMessage(thisDevice.accountId, text, System.currentTimeMillis() / 1000)

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkTextMessage).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendFileMessage(addressIp: String, thisDevice: Device, file: File) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect(InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_FILE_MESSAGE))

                val fileInputStream = FileInputStream(file)
                val buffer = ByteArray(1000000)
                fileInputStream.read(buffer)
                fileInputStream.close()

                thisDevice.ipAddress = socket.localAddress.hostAddress?.toString()
                val networkFileMessage = NetworkFileMessage(thisDevice.accountId, buffer, System.currentTimeMillis() / 1000)

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(networkFileMessage).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }
}