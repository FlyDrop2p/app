package com.flydrop2p.flydrop2p.network.service

import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.model.Keepalive
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

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(Keepalive(connectedDevices.toList() + thisDevice)).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendContentString(addressIp: String, device: Device, content: String) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_CONTENT_STRING)))

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(Pair(device, content)).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendFile(addressIp: String, file: File) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect(InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_FILE_TRANSFER))

                val outputStream = socket.getOutputStream()
                val fileInputStream = FileInputStream(file)

                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.close()
                fileInputStream.close()
            } catch (_: Exception) {
            }
        }
    }
}