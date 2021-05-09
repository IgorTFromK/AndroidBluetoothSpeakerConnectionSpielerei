package com.example.bluetoothspeakerconnectionspielerei

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.util.*
import java.util.concurrent.BlockingQueue

/**
 * MicAudioRecorder takes care of Reading Audiosamples from Microphone
 **/

class MicAudioRecord(
    sampleQueue: BlockingQueue<FloatArray>,
    sampleRate: Int,
    audioBufferSize: Int
) : Runnable {


    private var mAudioRecord: AudioRecord? = null
    private lateinit var mThread: Thread
    private var mSampleQueue: BlockingQueue<FloatArray> = sampleQueue
    private var mSampleRate: Int = sampleRate
    private var mAudioBufferSize: Int = audioBufferSize

    @Volatile
    private var mRecordingFlag: Boolean = false;

    companion object {
        private const val LOG_TAG = "MicAudioRecord: "
    }


    private fun initAudioRecorder() {
        val bufferSize: Int = AudioRecord.getMinBufferSize(mSampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT)
        Log.i(LOG_TAG, "Buffersize: ${bufferSize}")
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, mSampleRate, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_FLOAT,  mAudioBufferSize * Float.SIZE_BYTES)

        /*
        if (mAudioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            throw RuntimeException("Failed to initialize recorder")
        }

         */
        Log.i(LOG_TAG, "-------------------------");
        Log.i(LOG_TAG, "AudioRecorder initialized");
        Log.i(LOG_TAG, "State: ${mAudioRecord?.state}")
    }

    /**
     * Starts thread and also recording audio
     */

    fun startRecording() {
        mThread = Thread(this)
        mRecordingFlag = true
        initAudioRecorder()
        Log.i(LOG_TAG, "AudioRecorder started");
        mAudioRecord?.startRecording()
        mThread.start()
    }


    /**
     * Stops thread and also recording audio
     */
    fun stopRecording() {
        mRecordingFlag = false
        if (mAudioRecord != null) {
            Log.i(LOG_TAG, "AudioRecorder stopped");
            mAudioRecord?.stop();
            mAudioRecord?.release();
            mSampleQueue?.clear();
            Log.i(LOG_TAG, "AudioRecorder stopped");

        }
    }


    /**
     * Reading Audiosamples from Microphone/AudioRecord object is done here
     */
    override fun run() {
        val buffer = FloatArray(mAudioBufferSize)
        var i: Int = 0
        while (mRecordingFlag) {
            val read: Int? = mAudioRecord?.read(
                buffer, 0, mAudioBufferSize,
                AudioRecord.READ_BLOCKING
            )

            if (i % 10 == 0) {
                Log.i(LOG_TAG, "${read}  Samples was read ; " + Arrays.toString(buffer))
                Log.i(
                    LOG_TAG,
                    "Number of reads: ${i} Queuesize: ${mSampleQueue.size} Buffersize ${buffer.size}"
                )
            }
            mSampleQueue.offer(buffer)
            i++
        }

    }
}