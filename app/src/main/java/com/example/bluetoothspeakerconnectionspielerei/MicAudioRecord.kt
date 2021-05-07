package com.example.bluetoothspeakerconnectionspielerei

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.util.concurrent.BlockingQueue

/**
 * MicAudioRecorder takes care of Reading Audiosamples from Microphone
 **/

class MicAudioRecord(
    sampleQueue: BlockingQueue<FloatArray>,
    sampleRate: Int,
    audioBufferSize: Int
) : Runnable {


    private var audioRecord: AudioRecord? = null
    private lateinit var thread: Thread
    private var sampleQueue: BlockingQueue<FloatArray> = sampleQueue
    private var sampleRate: Int = sampleRate
    private var audioBufferSize: Int = audioBufferSize

    @Volatile
    private var recordingFlag: Boolean = false;

    companion object {
        private const val LOG_TAG = "MicAudioRecord: "
    }


    private fun initAudioRecorder() {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_FLOAT, audioBufferSize * Float.SIZE_BYTES
        )
        if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            throw RuntimeException("Failed to initialize recorder")
        }
        Log.i(LOG_TAG, "-------------------------");
        Log.i(LOG_TAG, "AudioRecorder initialized");
    }

    /**
     * Starts thread and also recording audio
     */

    fun startRecording() {
        thread = Thread(this)
        recordingFlag = true
        initAudioRecorder()
        Log.i(LOG_TAG, "AudioRecorder started");
        audioRecord?.startRecording()
        thread.start()
    }

    /**
     * Stops thread and also recording audio
     */

    fun stopRecording() {
        recordingFlag = false
        if (audioRecord != null) {
            Log.i(LOG_TAG, "AudioRecorder stopped");
            audioRecord?.stop();
            audioRecord?.release();
            sampleQueue?.clear();
            Log.i(LOG_TAG, "AudioRecorder stopped");

        }
    }

    /**
     * Reading Audiosamples from Microphone/AudioRecord object is done here
     */


    override fun run() {
        TODO("Not yet implemented")
        val buffer = FloatArray(audioBufferSize)
        var i: Int = 0
        while (recordingFlag) {
            val read: Int? = audioRecord?.read(
                buffer, 0, audioBufferSize,
                AudioRecord.READ_BLOCKING
            )
            /**
            if (i % 1 == 0) {
            Log.i(LOG_TAG, read + " Samples was read ; " + Arrays.toString(buffer));
            Log.i(LOG_TAG, read + " Samples was read ");
            Log.i(LOG_TAG, "Number of reads: " + i + " Queuesize: " + sampleQueue.size() + " Buffersize " + buffer.length);
            Log.i(LOG_TAG, "Min Buffersize: " + audioRecordMinBufferSize);
            }
             */
        }
        sampleQueue.offer(buffer)
        i++
    }
}