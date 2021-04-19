package com.example.bluetoothspeakerconnectionspielerei

import android.bluetooth.BluetoothDevice

interface OnItemClickListener {
    fun onItemClick(bluetoothDevice: BluetoothDevice?)
}