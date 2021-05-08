package com.example.bluetoothspeakerconnectionspielerei

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class VoiceDistorterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mRecordingButton: ToggleButton

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
            Log.d(LOG_TAG, "Recording wird gestartet !")
        } else{
            Log.d(LOG_TAG, "Recording wird beendet !");
        }
    }
}