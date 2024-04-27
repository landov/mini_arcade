package com.badlogic.androidgames.jumper


import android.annotation.SuppressLint
import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber
//import com.hoho.android.usbserial.examples.CustomProber
import java.util.Locale

class MainActivity : ListActivity() {

    private val baudRate = 19200
    private val withIoManager = true

    class ListItem(var device: UsbDevice, var port: Int, var driver: UsbSerialDriver?) {

    }

    private val devices = ArrayList<ListItem>()
    private lateinit var deviceListAdapter: ArrayAdapter<ListItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceListAdapter = object : ArrayAdapter<ListItem>(this, 0, devices) {
            @SuppressLint("SetTextI18n")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val item = devices.get(position)
                var view = convertView
                if (view == null)
                    view = layoutInflater.inflate(R.layout.device_list_item, parent, false)
                val text1: TextView = view!!.findViewById(R.id.text1)
                val text2: TextView = view.findViewById(R.id.text2)
                if (item.driver == null)
                    text1.text = "<no drivers"
                else
                    if (item.driver!!.ports.size == 1)
                        text1.text =
                            item.driver!!
                                .javaClass
                                .simpleName
                                .replace("SerialDriver", "")
                    else
                        text1.text =
                            item.driver!!
                                .javaClass
                                .simpleName
                                .replace("SerialDriver", "") + ", Port " + item.port.toString()
                text2.text = String.format(
                    Locale.US,
                    "Vendor %04X, Product %04X %04X",
                    item.device.vendorId,
                    item.device.productId,
                    item.device.deviceId
                )
                return view
            }
        }
        listAdapter = deviceListAdapter
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    fun refresh() {
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val usbDefaultProber = UsbSerialProber.getDefaultProber()
       // val usbCustomProber = CustomProber.getCustomProber()
        devices.clear()
        usbManager.deviceList.values.forEach { device ->
            var driver: UsbSerialDriver? = usbDefaultProber.probeDevice(device)
       /*     if (driver == null) {
                driver = usbCustomProber.probeDevice(device)
            }*/
            if (driver != null) {
                for (port in 0..<driver.ports.size) {
                    devices.add(ListItem(device, port, driver))
                }
            } else {
                devices.add(ListItem(device, 0, null))
            }
        }
        deviceListAdapter.notifyDataSetChanged()
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val item = devices.get(position)
        if(item.driver == null){
            Toast.makeText(this, "no driver", Toast.LENGTH_SHORT).show()
            return
        }
        val args = Bundle()

        args.putInt("device", item.device.deviceId)
        args.putInt("port", item.port)
        args.putInt("baud",baudRate)
        args.putBoolean("withIoManager",withIoManager)

        val intent = Intent(this,SuperJumper::class.java)
        intent.putExtras(args)
        startActivity(intent)

    }
}