package com.flydrop2p.flydrop2p.media

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import java.io.File

class VideoManager(private val context: Context) : MediaManager {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override var isRecording: Boolean = false
        private set

    override var isPlaying: Boolean = false
        private set

    var recordingFileUri: Uri = Uri.EMPTY

    override fun startRecording() {
        recordingFileUri = Uri.fromFile(File(context.filesDir, "audio_${System.currentTimeMillis()}.mp4"))

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.CAMERA)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP)
            setOutputFile(recordingFileUri.path)
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

    override fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }

        isRecording = false
        recorder = null
    }

    override fun startPlaying(fileName: String) {
        player = MediaPlayer().apply {
            setDataSource(File(context.filesDir, fileName).path)
            prepare()
            start()
        }

        isPlaying = true
    }

    override fun stopPlaying() {
        player?.apply {
            stop()
            release()
        }

        isPlaying = false
        player = null
    }
}