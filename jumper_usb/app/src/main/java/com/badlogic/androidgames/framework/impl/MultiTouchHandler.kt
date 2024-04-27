package com.badlogic.androidgames.framework.impl

import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.badlogic.androidgames.framework.Input.TouchEvent

class MultiTouchHandler(view: View, val scaleX: Float, val scaleY: Float) : TouchHandler {

    private val MAX_TOUCHPOINTS = 10
    private val isTouched = BooleanArray(MAX_TOUCHPOINTS)
    private val touchX = IntArray(MAX_TOUCHPOINTS)
    private val touchY = IntArray(MAX_TOUCHPOINTS)
    private val id = IntArray(MAX_TOUCHPOINTS)
    private val factory = object : Pool.PoolObjectFactory<TouchEvent> {
        override fun createObject(): TouchEvent = TouchEvent()
    }
    private val touchEventPool = Pool<TouchEvent>(factory, 100)
    private val touchEvents = mutableListOf<TouchEvent>()
    private val touchEventsBuffer = mutableListOf<TouchEvent>()

    init {
        view.setOnTouchListener(this)
    }

    override fun isTouchDown(pointer: Int): Boolean {
        synchronized(this) {
            val index = getIndex(pointer)
            if (index < 0 || index >= MAX_TOUCHPOINTS) return false
            else return isTouched[index]
        }
    }

    override fun getTouchX(pointer: Int): Int {
        synchronized(this) {
            val index = getIndex(pointer)
            if (index < 0 || index >= MAX_TOUCHPOINTS) return 0
            else return touchX[index]
        }
    }

    override fun getTouchY(pointer: Int): Int {
        synchronized(this) {
            val index = getIndex(pointer)
            if (index < 0 || index >= MAX_TOUCHPOINTS) return 0
            else return touchY[index]
        }
    }

    override fun getTouchEvents(): List<TouchEvent> {
        synchronized(this) {
            touchEvents.forEach(touchEventPool::free)
            touchEvents.clear()
            touchEvents.addAll(touchEventsBuffer)
            touchEventsBuffer.clear()
            return touchEvents
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        synchronized(this) {

            val action = event.action and MotionEvent.ACTION_MASK

            val pointerIndex =
                (event.action and MotionEvent.ACTION_POINTER_INDEX_MASK) shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
            val pointerCount = event.pointerCount
            var touchEvent: TouchEvent?
            for (i: Int in 0..<MAX_TOUCHPOINTS) {
                if (i >= pointerCount) {
                    isTouched[i] = false
                    id[i] = -1
                    continue
                }
                val pointerId = event.getPointerId(i)
                if (event.action != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    continue
                }
                touchEvent = touchEventPool.newObject()
                touchEvent.pointer = pointerId
                touchX[i] = (event.getX(i) * scaleX).toInt()
                touchEvent.x = touchX[i]
                touchY[i] = (event.getY(i) * scaleY).toInt()
                touchEvent.y = touchY[i]
                when (action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                        touchEvent.type = TouchEvent.TOUCH_DOWN
                        isTouched[i] = true
                        id[i] = pointerId
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP,
                    MotionEvent.ACTION_CANCEL,
                    -> {
                        touchEvent.type = TouchEvent.TOUCH_UP
                        isTouched[i] = false
                        id[i] = -1

                    }

                    MotionEvent.ACTION_MOVE -> {

                        touchEvent.type = TouchEvent.TOUCH_DRAGGED
                        isTouched[i] = true
                        id[i] = pointerId

                    }
                }

                touchEventsBuffer.add(touchEvent)


            }
            return true
        }
    }

    private fun getIndex(pointerId: Int): Int {
        for (i in 0..<MAX_TOUCHPOINTS) {
            if (id[i] == pointerId)
                return i
        }
        return -1

    }
}