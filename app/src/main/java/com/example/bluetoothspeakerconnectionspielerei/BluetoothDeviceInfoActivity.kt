package com.example.bluetoothspeakerconnectionspielerei

import android.annotation.SuppressLint
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.bluetoothspeakerconnectionspielerei.util.Constants
import java.lang.String
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


//TODO: Load Icon
//TODO: Switch Activity
//TODO: BroadcastReceiver getting notified when bluetooth connection is established
//TODO: Use Butterknife
class BluetoothDeviceInfoActivity : AppCompatActivity(), BluetoothBroadcastReceiver.Callback,
        BluetoothA2DPRequester.Callback, View.OnClickListener {

    private  var  mAdapter: BluetoothAdapter? = null
    private  var  mBluetoothDevice : BluetoothDevice? = null

    //View elements
    private lateinit var txtViewName : TextView
    private lateinit var txtViewAddress: TextView
    private lateinit var txtViewBondState : TextView
    private lateinit var txtViewMajorClass :TextView
    private lateinit var txtViewType : TextView
    private lateinit var txtViewUUID : TextView
    private lateinit var buttonConnect : Button

    companion object {
        private const val LOG_TAG = "BluetoothDeviceInfoActivity:"
        const val EXTRA_MESSAGE = "com.example.bluetoothspeakerconnectionspielerei.BluetoothDeviceActivity"
        const val SAVE_STATE_BLUETOOTH_DEVICE = "bluetoothDevice"
    }

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_bluetooth_device_info_activity)
        val intent_obj = intent.extras?.getParcelable(MainActivity.EXTRA_MESSAGE)
                as? BluetoothDevice
        if(intent_obj != null){
            mBluetoothDevice = intent_obj
        }

        mAdapter = BluetoothAdapter.getDefaultAdapter()
        initViewElems()
    }


    override fun onResume() {
        super.onResume()
        setBluetoothDeviceInfosToTextViewElems()
    }

    private fun initViewElems(){
        txtViewName = findViewById(R.id.txt_view_dev_name)
        txtViewAddress = findViewById(R.id.txt_view_dev_address)
        txtViewBondState = findViewById(R.id.txt_view_dev_bond_state)
        txtViewMajorClass = findViewById(R.id.txt_view_dev_major_class)
        txtViewType = findViewById(R.id.txt_view_dev_type)
        txtViewUUID = findViewById(R.id.txt_view_dev_uuid)

        buttonConnect = findViewById(R.id.button_connect)
        buttonConnect.setOnClickListener(this)
    }

    @SuppressLint("LongLogTag")
    private fun setBluetoothDeviceInfosToTextViewElems(){
        txtViewName.text = if (mBluetoothDevice?.name == null) Constants.UNKNOWN_DEVICE_NAME
        else mBluetoothDevice?.name
        txtViewAddress.text = mBluetoothDevice?.address
        txtViewBondState.text = Constants.BONDING_STATE[mBluetoothDevice?.bondState]
        txtViewType.text = Constants.BLUETOOTH_DEVICE_TYPE[mBluetoothDevice?.type]
        txtViewMajorClass.text = Constants.BLUETOOTH_DEVICE_MAJOR_CLASS[mBluetoothDevice?.bluetoothClass
                ?.majorDeviceClass]
        txtViewUUID.text = ""
        val uuids = mBluetoothDevice?.uuids
        if (uuids != null) {
            for (uuid: ParcelUuid in uuids) {
                val tmp = txtViewUUID.text
                val formatUUID = String.format("%s\r\n", uuid)
                txtViewUUID.text = "$tmp   $formatUUID"
            }
        }
        Log.d(LOG_TAG, "Hier bin ich")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVE_STATE_BLUETOOTH_DEVICE, mBluetoothDevice)
        val a = "WTF"
        outState.putString("hello", a)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val a = savedInstanceState.getString("hello")
        Toast.makeText(this@BluetoothDeviceInfoActivity, a, Toast.LENGTH_SHORT).show()
    }




    override fun onBluetoothConnected() {
        BluetoothA2DPRequester(this).request(this, mAdapter!!);
    }

    @SuppressLint("LongLogTag")
    override fun onBluetoothError() {
        Log.e(LOG_TAG, "There was an error enabling the Bluetooth Adapter.");
    }

    @SuppressLint("LongLogTag")
    override fun onA2DPProxyReceived(proxy: BluetoothA2dp?) {
        val connect: Method? = getConnectMethod()

        if (connect == null || mBluetoothDevice == null) {
            return;
        }
        val address = mBluetoothDevice?.address
        if (proxy != null && address != null) {
            for(device in proxy.connectedDevices){
                if(address.compareTo(device.address) == 0){
                    Log.d(LOG_TAG, "Connection with $address already established !")
                    val myIntent = Intent(this@BluetoothDeviceInfoActivity, OsciliatorActivity::class.java)
                    this.startActivity(myIntent)
                    return
                }
            }
                try {
                    connect.isAccessible = true
                    connect.invoke(proxy, mBluetoothDevice)
                } catch (ex: InvocationTargetException) {
                    Log.e(LOG_TAG, "Unable to invoke connect(BluetoothDevice) method on proxy. " + ex.toString())
                } catch (ex: IllegalAccessException) {
                    Log.e(LOG_TAG, "Illegal Access! $ex")
                }
        }

    }

    /**
     * Wrapper around some reflection code to get the hidden 'connect()' method
     * @return the connect(BluetoothDevice) method, or null if it could not be found
     */
    @SuppressLint("LongLogTag")
    private fun getConnectMethod(): Method? {
         try {
         return   BluetoothA2dp::class.java.getDeclaredMethod("connect", BluetoothDevice::class.java)
        } catch (ex: NoSuchMethodException) {
             Log.e(LOG_TAG, "Unable to find connect(BluetoothDevice) method in BluetoothA2dp proxy.");
             return null
        }
    }

    @SuppressLint("LongLogTag")
    override fun onClick(v: View?) {
        if(mAdapter?.isEnabled == true){
            onBluetoothConnected();
            return;
        }
        //Check if we're allowed to enable Bluetooth. If so, listen for a
        //successful enabling
        if(mAdapter?.enable() == true){
            BluetoothBroadcastReceiver.register(this, this);
        } else {
            Log.e(LOG_TAG, "Unable to enable Bluetooth. Is Airplane Mode enabled?");
        }
    }

    @SuppressLint("LongLogTag")
    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "Wurde zerstört")
    }

    @SuppressLint("LongLogTag")
    override fun onStop() {
        super.onStop()
        Log.d(LOG_TAG, "Wurde gestoppt")
    }


}