package com.example.bluetoothspeakerconnectionspielerei

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Permission stuff
    private var permissionForBluetooth = false
    private var permissionForBluetoothAdmin = false
    private var permissionToAccessCoarseLocation = false
    private val permissions = arrayOf(Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION)

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var  bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    private var deviceCounter:Int = 0

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback(){
        @RequiresApi(Build.VERSION_CODES.R)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            val device : BluetoothDevice? = result?.device
            val bluetoothText: String = String.format("%d - %s, %s, %s\r\n", deviceCounter,
            result?.scanRecord?.deviceName, device?.address, device?.uuids.toString())
            Log.d(LOG_TAG, bluetoothText);
            val txtViewText = txtDevsOverview.text
            txtDevsOverview.text = "$txtViewText $bluetoothText"
            deviceCounter++;

        }
    }

    companion object {
        private const val PERMISSIONS_MULTIPLE_REQUEST = 123
        private const val LOG_TAG = "Logging: MainActivity: "
    }

    // Todo: Add Butterknife Framework for dependency injection View Elements
    // View elems
    private lateinit var txtDevsOverview: TextView
    private lateinit var scanBtn: Button;


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDevsOverview = findViewById(R.id.txtDevOverview)
        txtDevsOverview.movementMethod = ScrollingMovementMethod()
        scanBtn = findViewById(R.id.btnScan)
        scanBtn.setOnClickListener(this);

        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_MULTIPLE_REQUEST)

        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.adapter

        if (bluetoothAdapter != null && !bluetoothAdapter?.isEnabled!!) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 10)
        }

        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    }


    //Todo: change the permisson fuck stuff
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_MULTIPLE_REQUEST -> {
                permissionForBluetooth = grantResults[0] == PackageManager.PERMISSION_GRANTED
                permissionForBluetoothAdmin = grantResults[1] == PackageManager.PERMISSION_GRANTED
                permissionToAccessCoarseLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!permissionForBluetooth|| ! permissionForBluetoothAdmin || !permissionToAccessCoarseLocation) {
            finish()
        }
    }

    private fun scanLeDevice() {
        bluetoothLeScanner?.let { scanner ->
            if (!scanning) { // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    scanning = false
                    scanner.stopScan(leScanCallback)
                }, SCAN_PERIOD)
                scanning = true
                scanner.startScan(leScanCallback)
            } else {
                scanning = false
                scanner.stopScan(leScanCallback)
            }
        }
    }


    override fun onClick(v: View?) {
        txtDevsOverview.text = ""
        scanLeDevice()
    }
}