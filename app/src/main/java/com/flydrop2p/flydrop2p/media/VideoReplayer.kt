package com.flydrop2p.flydrop2p.media

import android.media.MediaRecorder
import java.io.File

class VideoReplayer : MediaReplayer() {
    override fun startRecording(file: File) {
        recordingFile = file

        recorder = MediaRecorder().apply {
            setVideoSource(MediaRecorder.VideoSource.CAMERA)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP)
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