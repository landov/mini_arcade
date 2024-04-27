package com.badlogic.androidgames.framework.impl

import android.view.MotionEvent
import android.view.View
import com.badlogic.androidgames.framework.Input.TouchEvent
import com.badlogic.androidgames.framework.impl.Pool.PoolObjectFactory

class SingleTouchHandler(view: View, val scaleX: Float, val scaleY: Float) : TouchHandler {

    private var isTouched = false
    private var touchX = 0
    private var touchY = 0
    private val factory = object : PoolObjectFactory<TouchEvent> {
        override fun createObject(): TouchEvent = TouchEvent()
    }
    private val touchEventPool = Pool<TouchEvent>(factory, 100)
    private val touchEvents = mutableListOf<TouchEvent>()
    private val touchEventBuffer = mutableListOf<TouchEvent>()

    init {
        view.setOnTouchListener(this)
    }


    override fun isTouchDown(pointer: Int): Boolean {
        synchronized(this){
            if(pointer == 0) return isTouched
            else return false
        }
    }

    override fun getTouchX(pointer: Int): Int {
        synchronized(this){
            return touchX
        }
    }

    override fun getTouchY(pointer: Int): Int {
        synchronized(this){
            return  touchY
        }
    }

    override fun getTouchEvents(): List<TouchEvent> {
        synchronized(this){
            touchEvents.forEach(touchEventPool::free)
            touchEvents.clear()
            touchEvents.addAll(touchEventBuffer)
            touchEventBuffer.clear()
            return touchEvents
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        synchronized(this) {
            val touchEvent = touchEventPool.newObject()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchEvent.type = TouchEvent.TOUCH_DOWN
                    isTouched = true
                }
                MotionEvent.ACTION_MOVE -> {
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED
                    isTouched = true
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->{
                    touchEvent.type = TouchEvent.TOUCH_UP
                    isTouched = false
                }
            }
            touchX = (event.getX() * scaleX).toInt()
            touchEvent.x = touchX
            touchY = (event.getY() * scaleY).toInt()
            touchEvent.y = touchY
            touchEventBuffer.add(touchEvent)
            return true
        }
    }
}