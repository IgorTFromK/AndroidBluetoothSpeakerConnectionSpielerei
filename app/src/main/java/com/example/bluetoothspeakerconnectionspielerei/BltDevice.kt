package com.example.bluetoothspeakerconnectionspielerei

class BltDevice(name: String, val adress: String, val paired: String) {
    var devName: String = name
        set(value) {
            if (value == null) field = "Unknown Device Name"
        }
}
