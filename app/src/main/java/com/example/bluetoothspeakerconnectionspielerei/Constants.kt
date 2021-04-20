package com.example.bluetoothspeakerconnectionspielerei

import android.bluetooth.BluetoothDevice

class Constants private constructor(){
    companion object{
        @JvmField  val BONDING_STATE = mapOf(BluetoothDevice.BOND_NONE to "not bonded",
                BluetoothDevice.BOND_BONDING to "bonding", BluetoothDevice.BOND_BONDED to "bonded")
    }
}