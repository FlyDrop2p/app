package com.flydrop2p.flydrop2p.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.LoudnessEnhancer
import android.media.audiofx.NoiseSuppressor
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch


class CallManager(private val context: Context) {
    companion object {
        private const val SAMPLE_RATE = 44100
        private const val ENCODING = AudioFormat.ENCODING_PCM_16BIT
        private const val BUFFER_SIZE_FACTOR = 10
        private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, ENCODING) * BUFFER_SIZE_FACTOR
    }

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()

    private val audioFormat = AudioFormat.Builder()
        .setSampleRate(SAMPLE_RATE)
        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
        .setEncoding(ENCODING).build()

    private var audioTrack: AudioTrack? = null
    private var audioRecord: AudioRecord? = null
    private var enhancer: LoudnessEnhancer? = null

    private var recordingJob: Job? = null

    fun playAudio(audioBytes: ByteArray) {
        audioTrack?.write(audioBytes, 0, BUFFER_SIZE)
    }

    fun startPlaying() {
        audioTrack = AudioTrack(audioAttributes, audioFormat, BUFFER_SIZE, AudioTrack.MODE_STREAM, 0)

        audioTrack?.apply {
            enhancer = LoudnessEnhancer(audioSessionId)
            enhancer?.setTargetGain(10000)
            play()
        }
    }

    fun stopPlaying() {
        audioTrack?.stop()
        audioTrack = null
        enhancer = null
    }

    fun enableSpeakerVolume() {
        enhancer?.setEnabled(true)
    }

    fun disableSpeakerVolume() {
        enhancer?.setEnabled(false)
    }

    fun startRecording(handleAudioBytes: (ByteArray) -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            audioRecord = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, ENCODING, BUFFER_SIZE)

            audioRecord?.let {
                it.startRecording()
                val buffer = ByteArray(BUFFER_SIZE)

                recordingJob = CoroutineScope(Dispatchers.IO).launch {
                    while(recordingJob?.isActive == true) {
                        it.read(buffer, 0, BUFFER_SIZE)
                        handleAudioBytes(buffer)
                    }
                }
            }
        }
    }

    suspend fun stopRecording() {
        recordingJob?.cancelAndJoin()
        recordingJob = null

        audioRecord?.apply {
            stop()
            release()
            audioRecord = null
        }
    }
}
