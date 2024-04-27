package com.badlogic.androidgames.framework

interface Input {
    class KeyEvent {


        var type: Int = 0
        var keyCode = 0
        var keyChar = 0 //We keep it Int
        var x: Int = 0
        var y: Int = 0
        var pointer: Int = 0

        companion object{
            val KEY_DOWN = 0
            val KEY_UP = 1
        }
    }

    class TouchEvent {
        var type: Int = 0
        var x: Int = 0
        var y: Int = 0
        var pointer: Int = 0

        companion object {
            val TOUCH_DOWN = 0
            val TOUCH_UP = 1
            val TOUCH_DRAGGED = 2
        }
    }

    fun isKeyPressed(keyCode: Int): Boolean
    fun isTouchDown(pointer: Int): Boolean
    fun getTouchX(pointer: Int): Int
    fun getTouchY(pointer: Int): Int
    fun getAccelX(): Float
    fun getAccelY(): Float
    fun getAccelZ(): Float
    fun getKeyEvents(): List<KeyEvent>
    fun getTouchEvents(): List<TouchEvent>
}