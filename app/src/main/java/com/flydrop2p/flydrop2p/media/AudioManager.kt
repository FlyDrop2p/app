package com.flydrop2p.flydrop2p.media

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import java.io.File

class AudioManager(private val context: Context) {
    private var recorder: MediaRecorder? = null
    var isRecording: Boolean = false
        private set

    var fileUri: Uri = Uri.EMPTY

    fun startRecording() {
        fileUri = Uri.fromFile(File(context.filesDir, "audio_${System.currentTimeMillis()}.3gp"))

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(fileUri.path)
        }

        recorder?.apply {
            try {
                prepare()
                start()
                isRecording = true
            } catch (_: Exception) {

            }
        }
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
            isRecording = false
        }

        recorder = null
    }

    fun startReproducing(fileName: String) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(File(context.filesDir, fileName).path)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}