package com.badlogic.androidgames.framework.impl

import android.content.Context
import android.view.View
import com.badlogic.androidgames.framework.Input


class AndroidInput(context: Context, view: View, scaleX: Float, scaleY: Float) : Input {

    private val accelHandler = AccelerometerHandler(context)
    private val keyHandler = KeyBoardHandler(view)
    private val touchHandler = MultiTouchHandler(view, scaleX, scaleY)

    override fun isKeyPressed(keyCode: Int): Boolean = keyHandler.isKeyPressed(keyCode)

    override fun isTouchDown(pointer: Int): Boolean = touchHandler.isTouchDown(pointer)

    override fun getTouchX(pointer: Int): Int = touchHandler.getTouchX(pointer)

    override fun getTouchY(pointer: Int): Int = touchHandler.getTouchY(pointer)

    override fun getAccelX(): Float = accelHandler.accelX

    override fun getAccelY(): Float = accelHandler.accelY

    override fun getAccelZ(): Float = accelHandler.accelZ

    override fun getKeyEvents(): List<Input.KeyEvent> = keyHandler.getKeyEvents()

    override fun getTouchEvents(): List<Input.TouchEvent> = touchHandler.getTouchEvents()

}