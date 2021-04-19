package com.example.bluetoothspeakerconnectionspielerei

import android.bluetooth.BluetoothDevice

class Constants  {
    companion object{
        val bondingState = mapOf(BluetoothDevice.BOND_NONE to "not bonded",
                BluetoothDevice.BOND_BONDING to "bonding", BluetoothDevice.BOND_BONDED to "bonded")
    }
}