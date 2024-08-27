package com.flydrop2p.flydrop2p.network

import com.flydrop2p.flydrop2p.data.local.FileManager
import com.flydrop2p.flydrop2p.media.AudioReplayer
import com.flydrop2p.flydrop2p.media.VideoReplayer
import java.io.File

class CallManager(private val fileManager: FileManager) {
    private val audioReplayer = AudioReplayer()
    private val videoReplayer = VideoReplayer()

    fun startRecording() {
        audioReplayer.startRecording(fileManager.getAudioTempFile())
        // videoReplayer.startRecording(fileManager.getVideoTempFile())
    }

    fun stopRecording() {
        audioReplayer.stopRecording()
        // videoReplayer.stopRecording()
    }

    fun sampleAudioRecording(): File? {
        val file = audioReplayer.recordingFile

        audioReplayer.recordingFile?.let {
            audioReplayer.stopRecording()
            audioReplayer.startRecording(fileManager.getAudioTempFile())
        }

        return file
    }

//    fun sampleVideoRecording(): File? {
//        videoReplayer.apply {
//            return recordingFile?.let { file ->
//                stopRecording()
//                startRecording(fileManager.getAudioTempFile())
//                file
//            }
//        }
//    }

    fun deleteRecordingFiles() {
        audioReplayer.recordingFile?.delete()
        // videoReplayer.recordingFile?.delete()
    }

    fun playAudio(file: File) {
        if(audioReplayer.isPlaying) {
            audioReplayer.stopPlaying()
        }

        audioReplayer.startPlaying(file)
    }
}