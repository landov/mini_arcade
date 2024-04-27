package com.badlogic.androidgames.framework.impl

import android.view.View.OnTouchListener
import com.badlogic.androidgames.framework.Input.TouchEvent

interface TouchHandler : OnTouchListener {

    fun isTouchDown(pointer: Int) : Boolean

    fun getTouchX(pointer: Int) : Int

    fun getTouchY(pointer: Int) : Int

    fun getTouchEvents(): List<TouchEvent>
}