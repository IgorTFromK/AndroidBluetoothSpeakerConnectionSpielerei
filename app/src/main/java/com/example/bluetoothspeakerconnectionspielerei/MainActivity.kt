package com.example.bluetoothspeakerconnectionspielerei

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    // Permission stuff
    private var permissionForBluetooth = false
    private var permissionForBluetoothAdmin = false
    private var permissionToAccessCoarseLoacation = false
    private val permissions = arrayOf(Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION)
    companion object {
        private const val PERMISSIONS_MULTIPLE_REQUEST = 123
    }

    // Todo: Add Butterknife Framework for dependency injection View Elements
    // View elems
    private lateinit var txtDevsOverview: TextView
    private lateinit var scanBtn: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDevsOverview = findViewById(R.id.txtDevOverview)
        scanBtn = findViewById(R.id.btnScan)

        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_MULTIPLE_REQUEST)


    }


    //Todo: change the permisson fuck stuff
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_MULTIPLE_REQUEST -> {
                permissionForBluetooth = grantResults[0] == PackageManager.PERMISSION_GRANTED
                permissionForBluetoothAdmin = grantResults[1] == PackageManager.PERMISSION_GRANTED
                permissionToAccessCoarseLoacation = grantResults[2] == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!permissionForBluetooth|| ! permissionForBluetoothAdmin || !permissionToAccessCoarseLoacation) {
            finish()
        }
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.BLUETOOTH), PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_ADMIN), PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PackageManager.PERMISSION_GRANTED);

    }

    private fun hasRequiredPermission() : Boolean{
        val hasBluetoothPermission : Boolean = hasPermission(Manifest.permission.BLUETOOTH);
        val hasBluetoothAdminPermission : Boolean = hasPermission(Manifest.permission.BLUETOOTH_ADMIN)
        val hasLocationPermission : Boolean = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        return hasBluetoothPermission && hasBluetoothAdminPermission && hasLocationPermission

    }

    private fun hasPermission(permission: String) : Boolean{
        return ActivityCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

}