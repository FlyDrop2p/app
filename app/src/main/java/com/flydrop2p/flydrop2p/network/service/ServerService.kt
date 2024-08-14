package com.flydrop2p.flydrop2p.network.service

import android.util.Log
import com.flydrop2p.flydrop2p.network.Device
import com.flydrop2p.flydrop2p.network.model.Keepalive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.net.ServerSocket

class ServerService {
    companion object {
        const val PORT_KEEPALIVE: Int = 8800
        const val PORT_CONTENT_STRING: Int = 8801
        const val PORT_FILE_TRANSFER: Int = 8802
    }

    suspend fun listenKeepalive(): Keepalive {
        val ret: Keepalive

        withContext(Dispatchers.IO) {
            val socket = ServerSocket(PORT_KEEPALIVE)
            val client = socket.accept()

            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            ret = Json.decodeFromString(buffer.decodeToString())

            socket.close()

            Log.d("KEEPALIVE", ret.toString())
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

    suspend fun receiveFile(destination: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val serverSocket = ServerSocket(PORT_FILE_TRANSFER)
                val client = serverSocket.accept()

                val inputStream = client.getInputStream()
                val fileOutputStream = FileOutputStream(destination)

                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead)
                }

                fileOutputStream.close()
                inputStream.close()
                serverSocket.close()
                true
            } catch (e: Exception) {
                Log.e("FILE TRANSFER", "Errore durante il trasferimento del file", e)
                false
            }
        }
    }
}
