package com.flydrop2p.flydrop2p.network

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class SendMessageService(private val context: Context) {
    private val socket = Socket()

    suspend fun send(address: InetAddress, message: String) {
        withContext(Dispatchers.IO) {
            try {
                /**
                 * Create a client socket with the host,
                 * port, and timeout information.
                 */
                socket.bind(null)
                socket.connect((InetSocketAddress(InetAddress.getByName("192.168.49.1"), 8888)))

                /**
                 * Create a byte stream from a JPEG file and pipe it to the output stream
                 * of the socket. This data is retrieved by the server device.
                 */
                val outputStream = socket.getOutputStream()
                outputStream.write(message.toByteArray())

                outputStream.close()
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            } finally {
                /**
                 * Clean up any open sockets when done
                 * transferring or if an exception occurred.
                 */
                socket.takeIf { it.isConnected }?.apply {
                    close()
                }
            }
        }
    }
}