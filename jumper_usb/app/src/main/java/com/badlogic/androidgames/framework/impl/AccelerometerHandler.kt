package com.badlogic.androidgames.framework.impl

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AccelerometerHandler(private val context : Context) : SensorEventListener {
    @Volatile
    private var _accelX = 0f
    val accelX : Float
        get() = _accelX
    @Volatile
    private var _accelY = 0f
    val accelY : Float
        get() = _accelY
    @Volatile
    private var _accelZ = 0f
    val accelZ : Float
        get() = _accelZ

    init{
        val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size != 0){
            val accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0)
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        _accelX = event.values[0]
        _accelY = event.values[1]
        _accelZ = event.values[2]
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //nothing to do here
    }


}