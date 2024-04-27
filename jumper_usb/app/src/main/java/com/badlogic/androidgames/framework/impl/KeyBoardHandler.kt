package com.badlogic.androidgames.framework.impl


import android.view.View
import android.view.View.OnKeyListener
import com.badlogic.androidgames.framework.Input.KeyEvent
import com.badlogic.androidgames.framework.impl.Pool.PoolObjectFactory

class KeyBoardHandler(view: View) : OnKeyListener {

    private val pressedKeys = BooleanArray(128)
    private val factory = object : PoolObjectFactory<KeyEvent> {
        override fun createObject(): KeyEvent {
            return KeyEvent()
        }
    }
    private val keyEventPool: Pool<KeyEvent> = Pool(factory, 100)
    private val keyEventsBuffer = mutableListOf<KeyEvent>()
    private val keyEvents = mutableListOf<KeyEvent>()

    init {
        view.setOnKeyListener(this)
        view.isFocusableInTouchMode = true
        view.requestFocus()
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        if (keyCode < 0 || keyCode > 127)
            return false
        return pressedKeys[keyCode]
    }

    fun getKeyEvents(): List<KeyEvent> {
        synchronized(this) {
            keyEvents.forEach { keyEvent ->
                keyEventPool.free(keyEvent)
            }
            keyEvents.clear()
            keyEvents.addAll(keyEventsBuffer)
            return keyEvents
        }
    }


    override fun onKey(v: View, keyCode: Int, event: android.view.KeyEvent): Boolean {
        if (event.action == android.view.KeyEvent.ACTION_MULTIPLE) return false

        synchronized(this) {
            val keyEvent = keyEventPool.newObject()
            keyEvent.keyCode = keyCode
            keyEvent.keyChar = event.unicodeChar
            if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN
                if (keyCode in 1..127) pressedKeys[keyCode] = true
            }
            if (event.action == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP
                if (keyCode in 1..127) pressedKeys[keyCode] = false
            }
            keyEventsBuffer.add(keyEvent)
        }
        return false
    }
}