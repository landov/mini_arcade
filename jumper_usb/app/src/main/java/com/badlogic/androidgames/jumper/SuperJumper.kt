package com.badlogic.androidgames.jumper

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.badlogic.androidgames.framework.Screen
import com.badlogic.androidgames.framework.impl.GLGame
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.nio.charset.StandardCharsets
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates

class SuperJumper : GLGame(), SerialInputOutputManager.Listener {

    private enum class UsbPermission {
        Unknown, Requested, Granted, Denied
    }

    private var deviceId = 0
    private var portNum by Delegates.notNull<Int>()
    private var baudRate by Delegates.notNull<Int>()
    // private var withIoManager by Delegates.notNull<Boolean>()
    private var connected = false
    private var usbSerialPort : UsbSerialPort? = null
    private var usbPermission = UsbPermission.Unknown

    private val INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB";

    private val broadcastReceiver : BroadcastReceiver
    private var usbIoManager : SerialInputOutputManager? = null
    private val spn = SpannableStringBuilder()

    var control : Float = 0f

    init {
        //Kell?
        broadcastReceiver =
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (INTENT_ACTION_GRANT_USB == intent.action) {
                        usbPermission = if (intent.getBooleanExtra(
                                UsbManager.EXTRA_PERMISSION_GRANTED,
                                false
                            )
                        ) UsbPermission.Granted else UsbPermission.Denied
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        deviceId = extras?.getInt("device",0) ?: 0
        portNum = extras?.getInt("port",0) ?: 0
        baudRate = extras?.getInt("baud",9600)?: 9600
    }

    var firstTimeCreate = true

    override fun getStartScreen(): Screen {
        return MainMenuScreen(this)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        super.onSurfaceCreated(gl, config)
        if(firstTimeCreate){
            Settings.load(getFileIO())
            Assets.load(this)
            firstTimeCreate = false
        } else {
            Assets.reload()
        }
    }

    override fun onStart() {
        super.onStart()
        ContextCompat.registerReceiver(this, broadcastReceiver, IntentFilter(INTENT_ACTION_GRANT_USB), ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onResume() {
        super.onResume()
        if(!connected && (usbPermission == UsbPermission.Unknown || usbPermission == UsbPermission.Granted))
            connect()
    }

    override fun onPause() {
        if(Settings.soundEnabled) Assets.music.pause()
        if(connected) disconnect()
        super.onPause()
    }

    override fun onStop() {
        unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    private fun connect(){
        var device : UsbDevice? = null
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        usbManager.deviceList.values.forEach { v ->
            if(v.deviceId == deviceId) device = v
        }
        if(device == null){
            toastIt("Connection failed device not found ${deviceId}")
            return
        }
        var driver : UsbSerialDriver? = UsbSerialProber.getDefaultProber().probeDevice(device)
        /*if(driver == null){
            driver = CustomProber.getCustomProber().probeDevice(device)
        }*/
        if(driver == null){
            toastIt("Connection failed: no driver for device")
            return
        }
        if(driver.ports.size < portNum){
            toastIt("Connection failed: not enough ports at device")
            return
        }

        usbSerialPort = driver.ports.get(portNum)
        val usbConnection : UsbDeviceConnection? = usbManager.openDevice(driver.device)
        if(usbConnection == null
            && usbPermission == UsbPermission.Unknown
            && !usbManager.hasPermission(driver.device)){
            usbPermission = UsbPermission.Requested
            val flags = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                PendingIntent.FLAG_MUTABLE else 0
            val intent = Intent(INTENT_ACTION_GRANT_USB)
            intent.`package` = packageName
            val usbPermissionIntent = PendingIntent.getBroadcast(this,0,intent,flags)
            usbManager.requestPermission(driver.device,usbPermissionIntent)
            return
        }

        if(usbConnection == null){
            if(!usbManager.hasPermission(driver.device))
                toastIt("Connection failed: permission denied")
            else
                toastIt("Connection failed: open failed")
            return
        }

        try{
            usbSerialPort!!.open(usbConnection)
            try{
                usbSerialPort!!.setParameters(baudRate,8,1,UsbSerialPort.PARITY_NONE)
            } catch (e : UnsupportedOperationException) {
                toastIt("unsupport setparameters")
            }
            //  val ioManager = SerialInputOutputManager(usbSerialPort,this)
            //  ioManager.start()
            usbSerialPort!!.rts = true
            val usbIoManager = SerialInputOutputManager(usbSerialPort, this);
            usbIoManager.start()
            connected = true
        } catch (e: Exception){
            toastIt("Connection failed ${e.message}")
            disconnect()
        }
    }

    fun disconnect(){
        connected = false
        usbSerialPort = null
        usbIoManager?.setListener(null);
        usbIoManager?.stop()
    }

    override fun onNewData(buffer: ByteArray) {

        spn.clear()
        spn.append(java.lang.String(buffer, StandardCharsets.UTF_8))
        try{
            val intValue = spn.toString().trim().toInt()
            control = intValue.toFloat()
            Log.d("USJUMP",control.toString())
        } catch (e: Exception){
            e.message?.let { Log.d("SUJUMPEX", it) }
        //    e.message?.let { toastIt(it) }
        }
      /*  runOnUiThread( object: Runnable{
            override fun run() {
                text.text = spn.toString()
            }

        })*/

    }

    override fun onRunError(e: Exception) {
//        e.message?.let { toastIt(it) }
    }

    fun toastIt(message: String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()

    }


}