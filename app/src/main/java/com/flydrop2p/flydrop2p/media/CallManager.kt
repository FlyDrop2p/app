package com.flydrop2p.flydrop2p.media

import android.content.Context
import android.media.MediaPlayer
import com.flydrop2p.flydrop2p.network.model.call.NetworkCall
import java.io.File

class CallManager(private val context: Context, private val fileManager: FileManager) {
    fun handleCall(networkCall: NetworkCall) {
        fileManager.saveNetworkCall(networkCall)?.let { fileName ->
            MediaPlayer().apply {
                setDataSource(File(context.filesDir, fileName).path)
                prepare()
                start()
            }
        }
    }
}