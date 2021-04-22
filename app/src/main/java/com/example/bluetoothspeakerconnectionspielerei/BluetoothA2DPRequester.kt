package com.example.bluetoothspeakerconnectionspielerei

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.ServiceListener
import android.content.Context

/**
 *
 * Copyright 2013 Kevin Coppock
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Sends an asynchronous request to the BluetoothAdapter for an instance of a
 * BluetoothA2dp proxy.
 */
class BluetoothA2DPRequester
/**
 * Creates a new instance of an A2DP Proxy requester with the
 * callback that should receive the proxy once it is acquired
 * @param callback the callback that should receive the proxy
 */(private val mCallback: Callback?) : ServiceListener {
    /**
     * Start an asynchronous request to acquire the A2DP proxy. The callback
     * will be notified when the proxy is acquired
     * @param c the context used to obtain the proxy
     * @param adapter the BluetoothAdapter that should receive the request for proxy
     */
    fun request(c: Context?, adapter: BluetoothAdapter) {
        adapter.getProfileProxy(c, this, BluetoothProfile.A2DP)
    }

    override fun onServiceConnected(i: Int, bluetoothProfile: BluetoothProfile) {
        mCallback?.onA2DPProxyReceived(bluetoothProfile as BluetoothA2dp)
    }

    override fun onServiceDisconnected(i: Int) {
        //It's a one-off connection attempt; we don't care about the disconnection event.
    }

    interface Callback {
        fun onA2DPProxyReceived(proxy: BluetoothA2dp?)
    }
}