package com.flydrop2p.flydrop2p.media

interface MediaManager {
    val isRecording: Boolean

    val isPlaying: Boolean

    fun startRecording()

    fun stopRecording()

    fun startPlaying(fileName: String)

    fun stopPlaying()
}