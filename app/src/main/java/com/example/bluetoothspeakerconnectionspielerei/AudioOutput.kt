package com.example.bluetoothspeakerconnectionspielerei

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import java.util.concurrent.BlockingQueue

class AudioOutput(sampleQueue: BlockingQueue<FloatArray>, sampleRate: Int, audioBufferSize: Int) :
    Runnable {

    private var mAudioTrack: AudioTrack? = null
    private lateinit var mThread: Thread
    private var mSampleQueue: BlockingQueue<FloatArray> = sampleQueue
    private var mSampleRate: Int = sampleRate
    private var mAudioBufferSize = audioBufferSize

    @Volatile
    private var mRecordingFlag: Boolean = false;

    companion object {
        private const val LOG_TAG = "AudioOutput: "
    }

    //TODO: use Builder https://developer.android.com/reference/android/media/AudioTrack
    private fun initAudioTrack() {
        mAudioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            mSampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_FLOAT,
            mAudioBufferSize * Float.SIZE_BYTES,
            AudioTrack.MODE_STREAM
        )
    }

    fun startPlaying() {
        mThread = Thread(this)
        mRecordingFlag = true
        initAudioTrack()
        Log.i(LOG_TAG, "AudioTrack started");
        mAudioTrack?.play()
        mThread.start()
    }


    fun stopPlaying() {
        mRecordingFlag = false
        mAudioTrack?.stop()
        mAudioTrack = null
        Log.i(LOG_TAG, "AudioTrack stopped");

    }

    override fun run() {
        var i: Int = 0
        while (mRecordingFlag) {
            val buffer: FloatArray = mSampleQueue.take()
            val write: Int? = mAudioTrack?.write(buffer, 0, buffer.size, AudioTrack.WRITE_BLOCKING)
            /**
            if (i % 1 == 0) {
            Log.i(LOG_TAG, read + " Samples was read ; " + Arrays.toString(buffer));
            Log.i(LOG_TAG, read + " Samples was read ");
            Log.i(LOG_TAG, "Number of reads: " + i + " Queuesize: " + sampleQueue.size() + " Buffersize " + buffer.length);
            Log.i(LOG_TAG, "Min Buffersize: " + audioRecordMinBufferSize);
            }
             */
        }
        i++
    }
}




