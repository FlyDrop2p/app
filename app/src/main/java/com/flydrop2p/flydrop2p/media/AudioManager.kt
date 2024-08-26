package com.flydrop2p.flydrop2p.media

import android.content.Context
import android.media.MediaRecorder
import java.io.File

class AudioManager(private val context: Context) {
    private var recorder: MediaRecorder? = null
    
    private fun startRecording(fileName: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(File(context.filesDir, fileName).path)
        }

        recorder?.apply {
            try {
                prepare()
                start()
            } catch (_: Exception) {

            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }

        recorder = null
    }
}