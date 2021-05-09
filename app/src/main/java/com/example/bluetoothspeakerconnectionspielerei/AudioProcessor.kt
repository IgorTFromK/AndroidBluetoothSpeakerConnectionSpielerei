package com.example.bluetoothspeakerconnectionspielerei

import android.util.Log
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import java.util.concurrent.BlockingQueue

class AudioProcessor(
    intputSampleQueue: BlockingQueue<FloatArray>,
    outputSampleQueue: BlockingQueue<FloatArray>,
    sampleRate: Int,
    audioBufferSize: Int
) : Runnable {

    private lateinit var mThread: Thread
    private var mInputSampleQueue: BlockingQueue<FloatArray> = intputSampleQueue
    private var mOutputSampleQueue: BlockingQueue<FloatArray> = outputSampleQueue
    private var mSampleRate: Int = sampleRate
    private var mAudioBufferSize = audioBufferSize
    private var mWsola: WaveformSimilarityBasedOverlapAdd = WaveformSimilarityBasedOverlapAdd(
        WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(0.5, sampleRate.toDouble())
    )
    private var audioEvent: AudioEvent = AudioEvent(TarsosDSPAudioFormat(sampleRate.toFloat(), 32, 1, true, true ))

    @Volatile
    private var mProcessingFlag: Boolean = false;

    companion object {
        private const val LOG_TAG = "AudioOutput: "
    }

    fun changeWaveformSimilarityBasedOverlapAddParams(tempo: Double) {
        mWsola.setParameters(
            WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(
                tempo,
                mSampleRate.toDouble()
            )
        )
    }

    fun startAudioProcessing() {
        mThread = Thread(this)
        mProcessingFlag = true
        Log.i(LOG_TAG, "AudioProcessor started")
        mThread.start()
    }


    fun stopAudioProcessing() {
        mProcessingFlag = false
        Log.i(LOG_TAG, "AudioRecorder stopped")
    }


    override fun run() {
        while(mProcessingFlag){
            val buffer: FloatArray = mInputSampleQueue.take()
            audioEvent.floatBuffer = buffer
            mWsola.process(audioEvent)
            mOutputSampleQueue.offer(audioEvent.floatBuffer)
        }
    }
}