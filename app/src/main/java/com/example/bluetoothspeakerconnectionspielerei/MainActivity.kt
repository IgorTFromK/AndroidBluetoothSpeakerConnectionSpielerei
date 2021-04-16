package com.example.bluetoothspeakerconnectionspielerei

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // Todo: Add Butterknife Framework for dependency injection View Elements
    private lateinit var txtDevsOverview: TextView
    private lateinit var scanBtn: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDevsOverview = findViewById(R.id.txtDevOverview)
        scanBtn = findViewById(R.id.btnScan)
        txtDevsOverview.text = """Mohandas Karamchand Gandhi (/ˈɡɑːndi, ˈɡændi/;[2] Hindustani:
            [ˈmoːɦəndaːs ˈkərəmtʃənd ˈɡaːndʱi] (About this soundlisten) asdkjaslkdjlas 
            sdlöjaksöldkasökdölaskkkkkkkkkkkkkkkkasdasöldäalsäd akakakakakakakaka """

    }
}