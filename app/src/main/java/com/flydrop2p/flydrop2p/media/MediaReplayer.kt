package com.flydrop2p.flydrop2p.media

import android.media.MediaPlayer
import android.media.MediaRecorder
import java.io.File

sealed class MediaReplayer {
    protected var recorder: MediaRecorder? = null
    protected var player: MediaPlayer? = null

    var isRecording: Boolean = false
    var isPlaying: Boolean = false
    var recordingFile: File? = null

    abstract fun startRecording(file: File)

    fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                release()
                isRecording = false
            }
        } catch (_: Exception) {

        }

        recorder = null
    }

    fun startPlaying(file: File) {
        player = MediaPlayer().apply {
            setDataSource(file.path)
            prepare()
            start()
        }

        isPlaying = true
    }

    fun stopPlaying() {
        player?.apply {
            stop()
            release()
        }

        isPlaying = false
        player = null
    }
}