package com.example.bluetoothspeakerconnectionspielerei

import android.content.BroadcastReceiver
import android.annotation.SuppressLint
import android.content.Intent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.bluetoothspeakerconnectionspielerei.BluetoothBroadcastReceiver
import android.content.IntentFilter
import android.util.Log
import java.lang.IllegalArgumentException

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
 */
class BluetoothBroadcastReceiver private constructor(callback: Callback) : BroadcastReceiver() {
    private val mCallback: Callback?
    @SuppressLint("LongLogTag")
    override fun onReceive(context: Context, intent: Intent) {
        if (BluetoothAdapter.ACTION_STATE_CHANGED != intent.action) {
            Log.v(TAG, "Received irrelevant broadcast. Disregarding.")
            return
        }

        //This is a State Change event, get the state extra, falling back to ERROR
        //if it isn't there (which shouldn't happen)
        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
        when (state) {
            BluetoothAdapter.STATE_CONNECTED -> {
                safeUnregisterReceiver(context, this)
                fireOnBluetoothConnected()
            }
            BluetoothAdapter.ERROR -> {
                safeUnregisterReceiver(context, this)
                fireOnBluetoothError()
            }
        }
    }

    private fun fireOnBluetoothConnected() {
        mCallback?.onBluetoothConnected()
    }

    private fun fireOnBluetoothError() {
        mCallback?.onBluetoothError()
    }

    interface Callback {
        fun onBluetoothConnected()
        fun onBluetoothError()
    }

    companion object {
        private const val TAG = "BluetoothBroadcastReceiver"

        /**
         * Convenience method to register a new instance of this receiver with the
         * necessary IntentFilter, which will notify the callback upon a successful
         * connection or error case.
         * @param callback the callback that should be notified on success or failure
         * of Bluetooth being enabled
         * @param c the context from which to register the receiver
         */
        fun register(callback: Callback, c: Context) {
            c.registerReceiver(BluetoothBroadcastReceiver(callback), filter)
        }

        private val filter: IntentFilter
            private get() = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

        /**
         * Convenience method to do a checked unregistration of a broadcast receiver. Occasionally
         * you can get into a state where the receiver was already unregistered, throwing an
         * IllegalArgumentException. This method just swallows that exception and logs the error.
         * @param c the context from which to unregister the receiver
         * @param receiver the receiver that should be unregistered if it is not already unregistered
         */
        @SuppressLint("LongLogTag")
        private fun safeUnregisterReceiver(c: Context, receiver: BroadcastReceiver) {
            try {
                c.unregisterReceiver(receiver)
            } catch (ex: IllegalArgumentException) {
                Log.w(TAG, "Tried to unregister BluetoothBroadcastReceiver that was not registered.")
            }
        }
    }

    init {
        mCallback = callback
    }
}