package com.example.bluetoothspeakerconnectionspielerei.util

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice

class Constants private constructor() {
    companion object {
        @JvmField
        val BONDING_STATE = mapOf(BluetoothDevice.BOND_NONE to "not bonded",
                BluetoothDevice.BOND_BONDING to "bonding", BluetoothDevice.BOND_BONDED to "bonded")
        @JvmField
        val BLUETOOTH_DEVICE_MAJOR_CLASS = mapOf(BluetoothClass.Device.Major.AUDIO_VIDEO to "Audio/Video",
                BluetoothClass.Device.Major.COMPUTER to "Computer", BluetoothClass.Device.Major.HEALTH to "Health",
                BluetoothClass.Device.Major.IMAGING to "Imaging", BluetoothClass.Device.Major.MISC to "Misc",
                BluetoothClass.Device.Major.NETWORKING to "Networking", BluetoothClass.Device.Major.PERIPHERAL to "Peripheral",
                BluetoothClass.Device.Major.PHONE to "Phone", BluetoothClass.Device.Major.TOY to "Toy",
                BluetoothClass.Device.Major.UNCATEGORIZED to "Uncategorized", BluetoothClass.Device.Major.WEARABLE to "Wearable")
        @JvmField
        val BLUETOOTH_DEVICE_TYPE = mapOf(BluetoothDevice.DEVICE_TYPE_CLASSIC to "BR/EDR", BluetoothDevice.DEVICE_TYPE_LE to "LE-only",
                BluetoothDevice.DEVICE_TYPE_LE to "LE-only", BluetoothDevice.DEVICE_TYPE_DUAL to "BR/EDR/LE", BluetoothDevice.DEVICE_TYPE_UNKNOWN to "Unknown")
        const val UNKNOWN_DEVICE_NAME = "Unknown Device Name"
    }
}