package com.flydrop2p.flydrop2p.media

import android.media.MediaRecorder
import java.io.File

class AudioReplayer : MediaReplayer() {
    override fun startRecording(file: File) {
        recordingFile = file

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(file.path)
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
}