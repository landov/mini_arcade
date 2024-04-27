package com.badlogic.androidgames.framework.impl

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class CompassHandler(private val context : Context) : SensorEventListener {
    @Volatile
    private var _yaw = 0f
    val yaw : Float
        get() = _yaw
    @Volatile
    private var _pitch = 0f
    val pitch : Float
        get() = _pitch
    @Volatile
    private var _roll = 0f
    val roll : Float
        get() = _roll
    private var mAccelerometer : Sensor? = null
    private var mMagnetometer : Sensor? = null
    private var mLastAccelerometer = FloatArray(3)
    private var mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private var mR = FloatArray(9)
    private var mOrientation = FloatArray(3)

    init{
        val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == mAccelerometer){
            System.arraycopy(event.values,0,mLastAccelerometer,0,event.values.size)
            mLastAccelerometerSet = true
        } else if(event.sensor == mMagnetometer){
            System.arraycopy(event.values,0,mLastMagnetometer,0,event.values.size)
            mLastMagnetometerSet = true
        }
        if(mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR,null,mLastAccelerometer,mLastMagnetometer)
            SensorManager.getOrientation(mR,mOrientation)
            _yaw = mOrientation[0]
            _pitch = mOrientation[1]
            _roll = mOrientation[2]
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //nothing to do here
    }


}