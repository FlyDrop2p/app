package com.flydrop2p.flydrop2p.network.service

import android.util.Log
import com.flydrop2p.flydrop2p.network.model.NetworkKeepalive
import com.flydrop2p.flydrop2p.network.model.NetworkTextMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.net.ServerSocket

class ServerService {
    companion object {
        const val PORT_KEEPALIVE: Int = 8800
        const val PORT_TEXT_MESSAGE: Int = 8801
        const val PORT_FILE_TRANSFER: Int = 8802
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
