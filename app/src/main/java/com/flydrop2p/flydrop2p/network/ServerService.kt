package com.flydrop2p.flydrop2p.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.ServerSocket

class ServerService {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _isHandshakeSocketOpen = MutableStateFlow(false)
    val isHandshakeSocketOpen: StateFlow<Boolean> get() = _isHandshakeSocketOpen.asStateFlow()

    companion object {
        const val PORT_HANDSHAKE: Int = 8800
    }

    fun startConnection() {
        coroutineScope.launch {
            val socket = ServerSocket(PORT_HANDSHAKE)

            _isHandshakeSocketOpen.value = true

            // Wait for client connections. This call blocks until a connection is accepted from a client.
            val client = socket.accept()

            // If this code is reached, a client has connected and transferred data.
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes()
            val address = InetAddress.getByAddress(buffer)

            Log.d("ServerService, Socket: ${PORT_HANDSHAKE}", address.hostAddress ?: "")
            socket.close()
            _isHandshakeSocketOpen.value = false
        }
    }
}
