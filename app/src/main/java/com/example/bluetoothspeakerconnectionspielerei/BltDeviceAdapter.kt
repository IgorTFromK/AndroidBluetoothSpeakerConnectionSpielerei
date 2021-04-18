package com.example.bluetoothspeakerconnectionspielerei

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BltDeviceAdapter(private val bltDevices: ArrayList<BltDevice>) :
        RecyclerView.Adapter<BltDeviceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtViewDeviceName: TextView = view.findViewById(R.id.txtViewDeviceName)
        val txtViewDeviceAdress: TextView = view.findViewById(R.id.txtViewDeviceAdress)
        val txtViewDevicePaired: TextView = view.findViewById(R.id.txtViewDevicePaired)
    }

    fun add(position: Int, bltDevice: BltDevice) {
        bltDevices.add(position, bltDevice)
        notifyItemInserted(position)
    }

    fun remove(postion: Int) {
        bltDevices.removeAt(postion)
        notifyItemRemoved(postion)
    }

    fun clear(){
        val size = bltDevices.size
        bltDevices.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_elem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bltDevice: BltDevice = bltDevices.get(position)
        holder.txtViewDeviceName.text = bltDevice.devName
        holder.txtViewDeviceAdress.text = bltDevice.adress
        holder.txtViewDevicePaired.text = bltDevice.paired
    }

    override fun getItemCount() = bltDevices.size

}