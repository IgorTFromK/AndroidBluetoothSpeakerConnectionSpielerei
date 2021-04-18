package com.example.bluetoothspeakerconnectionspielerei

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Permission stuff
    private var permissionForBluetooth = false
    private var permissionForBluetoothAdmin = false
    private var permissionToAccessCoarseLocation = false
    private val permissions = arrayOf(Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION)

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var counter = 0
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName  = if (device?.name == null) "Unknown Name" else device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    bltDeviceAdapter.add(BltDevice(deviceName, deviceHardwareAddress, "not paired"))
                    counter++;
                    Log.d(LOG_TAG, "asjdklasjdlkaskdl: " + counter)
                }
            }
        }
    }

    companion object {
        private const val PERMISSIONS_MULTIPLE_REQUEST = 123
        private const val LOG_TAG = "Logging: MainActivity: "
    }

    // Todo: Add Butterknife Framework for dependency injection View Elements
    // View elems
    private lateinit var scanBtn: Button;
    private lateinit var recyclerView: RecyclerView
    private lateinit var bltDeviceAdapter: BltDeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanBtn = findViewById(R.id.btnScan)
        scanBtn.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        bltDeviceAdapter = BltDeviceAdapter(arrayListOf<BltDevice>())
        recyclerView.adapter =bltDeviceAdapter
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_MULTIPLE_REQUEST)

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 10)
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
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
        if (!permissionForBluetooth || !permissionForBluetoothAdmin || !permissionToAccessCoarseLocation) {
            finish()
        }
    }

    override fun onClick(v: View?) {
        counter = 0
        bltDeviceAdapter.clear()
        bluetoothAdapter?.startDiscovery()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            bltDeviceAdapter.add(BltDevice(deviceName, deviceHardwareAddress, "device paired"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}