package com.flydrop2p.flydrop2p.network.service

import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.model.GuestKeepalive
import com.flydrop2p.flydrop2p.network.model.OwnerKeepalive
import com.flydrop2p.flydrop2p.network.wifidirect.WiFiDirectBroadcastReceiver.Companion.IP_GROUP_OWNER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ClientService {
    suspend fun sendKeepaliveToOwner(device: Device) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(IP_GROUP_OWNER), ServerService.PORT_KEEPALIVE_OWNER)))

                device.ipAddress = socket.localAddress.hostAddress?.toString()

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(OwnerKeepalive(device)).encodeToByteArray())
                outputStream.close()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun sendKeepaliveToGuest(addressIp: String, devices: List<Device>) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName(addressIp), ServerService.PORT_KEEPALIVE_GUEST)))

                val outputStream = socket.getOutputStream()
                outputStream.write(Json.encodeToString(GuestKeepalive(devices.toList())).encodeToByteArray())
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
}