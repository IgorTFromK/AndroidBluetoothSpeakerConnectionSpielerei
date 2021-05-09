package com.example.bluetoothspeakerconnectionspielerei

import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class VoiceDistorterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mRecordingButton: ToggleButton
    private val mSampleRate: Int = 44100
    private val mBufferSize: Int = mSampleRate / 2
    private val mSampleQueue1: BlockingQueue<FloatArray> = LinkedBlockingQueue()
    private val mSampleQueue2: BlockingQueue<FloatArray> = LinkedBlockingQueue()
    private val mMicAudioRecord : MicAudioRecord = MicAudioRecord(mSampleQueue1,44100,mBufferSize)
    private val mAudioProcessor: AudioProcessor = AudioProcessor(mSampleQueue1, mSampleQueue2, 44100, mBufferSize)
    private val mAudioOutput: AudioOutput = AudioOutput(mSampleQueue2, 44100, mBufferSize )

    companion object {
        private  val LOG_TAG = "VoiceDistorterActivity: "
    }


    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.voice_distorter_activity)

        mRecordingButton = findViewById(R.id.toggleButton)
        mRecordingButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(mRecordingButton.isChecked){
            mMicAudioRecord.startRecording()
            mAudioProcessor.startAudioProcessing()
            mAudioOutput.startPlaying()
        } else{
            mMicAudioRecord.stopRecording()
            mAudioProcessor.stopAudioProcessing()
            mAudioOutput.stopPlaying()
        }
    }
}