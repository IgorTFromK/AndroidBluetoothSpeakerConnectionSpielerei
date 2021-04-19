package com.example.bluetoothspeakerconnectionspielerei

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BltDeviceAdapter(private val bluetoothDevices: ArrayList<BluetoothDevice?>, private val listener: OnItemClickListener) :
        RecyclerView.Adapter<BltDeviceAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtViewDeviceName: TextView = view.findViewById(R.id.txt_view_device_name)
        private val txtViewDeviceAddress: TextView = view.findViewById(R.id.txt_view_device_adress)
        private val txtViewDevicePaired: TextView = view.findViewById(R.id.txt_view_device_paired)

        fun bind(bluetoothDevice: BluetoothDevice?, listener: OnItemClickListener) {
            txtViewDeviceName.text = if (bluetoothDevice?.name == null) "No name provided" else bluetoothDevice?.name
            txtViewDeviceAddress.text = bluetoothDevice?.address
            txtViewDevicePaired.text = "bisher noch leer"
            itemView.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    listener.onItemClick(bluetoothDevice)
                }

            })
        }
    }

    fun add(bluetoothDevice: BluetoothDevice?) {
        bluetoothDevices.add(bluetoothDevice)
        notifyItemInserted(bluetoothDevices.indexOf(bluetoothDevice))
    }

    fun remove(postion: Int) {
        bluetoothDevices.removeAt(postion)
        notifyItemRemoved(postion)
    }

    fun clear() {
        val size = bluetoothDevices.size
        bluetoothDevices.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_elem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device: BluetoothDevice? = bluetoothDevices[position]
        holder.bind(device, this.listener)
    }


    override fun getItemCount() = bluetoothDevices.size


}