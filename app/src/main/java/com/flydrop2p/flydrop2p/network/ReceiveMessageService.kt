package com.flydrop2p.flydrop2p.network

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.flydrop2p.flydrop2p.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket

class ReceiveMessageService(private var activity: MainActivity) {
    suspend fun receive() {
        withContext(Dispatchers.IO) {
            /**
             * Create a server socket.
             */
            val serverSocket = ServerSocket(8888)
            // activity.socket.postValue(true)

            /**
             * Wait for client connections. This call blocks until a
             * connection is accepted from a client.
             */
            val client = serverSocket.accept()

            /**
             * If this code is reached, a client has connected and transferred data
             */
            val inputStream = client.getInputStream()
            val buffer = inputStream.readBytes().decodeToString()
            // activity.content.value = buffer

            serverSocket.close()
            // activity.socket.postValue(false)
        }
    }
}
