package com.example.bluetoothspeakerconnectionspielerei

import android.media.AudioManager
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.android.AndroidAudioPlayer
import be.tarsos.dsp.io.android.AudioDispatcherFactory

class AudioProcessing(sampleRate: Int, bufferSize: Int) {

    private lateinit var mThread: Thread
    private val mSampleRate: Int = sampleRate
    private val mBufferSize: Int = bufferSize
    private var mDispatcher: AudioDispatcher =
        AudioDispatcherFactory.fromDefaultMicrophone(mSampleRate, mBufferSize, 0)
    private var mWsola = WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(
        1.0, mSampleRate.toDouble()
    ))
    private var mAndroidAudioPlayer: AndroidAudioPlayer = AndroidAudioPlayer(TarsosDSPAudioFormat(
        mSampleRate.toFloat(), 16, 1, true, true), mBufferSize, AudioManager.STREAM_MUSIC)

    private fun initPlaying(){
        mWsola.setDispatcher(mDispatcher)
        mDispatcher.addAudioProcessor(mWsola)
        mDispatcher.addAudioProcessor(mAndroidAudioPlayer)
    }

    fun startPlaying(){

    }


}