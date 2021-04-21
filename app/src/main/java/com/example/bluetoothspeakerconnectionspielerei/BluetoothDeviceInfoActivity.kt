package com.example.bluetoothspeakerconnectionspielerei

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.String


class BluetoothDeviceInfoActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "BluetoothDeviceInfoActivity: "
    }

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstance: Bundle?){
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_bluetooth_device_info_activity)

        val bluetoothDevice: BluetoothDevice? = intent.extras?.getParcelable(MainActivity.EXTRA_MESSAGE)
                as? BluetoothDevice

        val txtViewName = findViewById<TextView>(R.id.txt_view_dev_name)
        val txtViewAdress = findViewById<TextView>(R.id.txt_view_dev_address)
        val txtViewBondState = findViewById<TextView>(R.id.txt_view_dev_bond_state)
        val txtViewMajorClass = findViewById<TextView>(R.id.txt_view_dev_major_class)
        val txtViewType = findViewById<TextView>(R.id.txt_view_dev_type)
        val txtViewUUID = findViewById<TextView>(R.id.txt_view_dev_uuid)

        txtViewName.text = if (bluetoothDevice?.name == null) Constants.UNKNOWN_DEVICE_NAME
            else bluetoothDevice?.name
        txtViewAdress.text = bluetoothDevice?.address
        txtViewBondState.text = Constants.BONDING_STATE[bluetoothDevice?.bondState]
        txtViewType.text = Constants.BLUETOOTH_DEVICE_TYPE[bluetoothDevice?.type]
        txtViewMajorClass.text = Constants.BLUETOOTH_DEVICE_MAJOR_CLASS[bluetoothDevice?.bluetoothClass
                ?.majorDeviceClass]
        val uuids = bluetoothDevice?.uuids
        if (uuids != null) {
            for (uuid: ParcelUuid in uuids) {
                val tmp = txtViewUUID.text
                val formatUUID = String.format("%s\r\n", uuid)
                txtViewUUID.text = "$tmp   $formatUUID"
            }
        }
    }
}