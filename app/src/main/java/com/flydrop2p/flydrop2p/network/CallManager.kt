package com.flydrop2p.flydrop2p.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class CallManager(private val context: Context) {
    companion object {
        private const val SAMPLE_RATE_IN_HZ = 44100
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT
        private const val BUFFER_SIZE_FACTOR = 10
        private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT) * BUFFER_SIZE_FACTOR
    }

    private val audioTrack = AudioTrack(AudioManager.STREAM_VOICE_CALL, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM)
    private var audioRecord: AudioRecord? = null

    private val recordingInProgress = AtomicBoolean(false)
    private var recordingJob: Job? = null

    fun playAudio(audioBytes: ByteArray) {
        audioTrack.write(audioBytes, 0, BUFFER_SIZE)
    }

    fun startPlaying() {
        audioTrack.play()
    }

    fun stopPlaying() {
        audioTrack.stop()
    }

    fun startRecording(handleAudioBytes: (ByteArray) -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE_IN_HZ,
                CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE
            )

            audioRecord?.apply {
                startRecording()
                recordingInProgress.set(true)

                recordingJob = CoroutineScope(Dispatchers.IO).launch {
                    val buffer = ByteArray(BUFFER_SIZE)

                    while (recordingInProgress.get()) {
                        val result: Int = read(buffer, 0, BUFFER_SIZE)

                        if (result < 0) {
                            Log.d("AudioRecord", "Read error")
                        }

                        handleAudioBytes(buffer)
                    }
                }
            }
        }
    }

    fun stopRecording() {
        recordingInProgress.set(false)
        recordingJob?.cancel()

        audioRecord?.apply {
            stop()
            release()
            audioRecord = null
        }
    }
}
