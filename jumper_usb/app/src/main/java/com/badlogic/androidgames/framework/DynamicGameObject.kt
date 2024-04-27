package com.badlogic.androidgames.framework

import com.badlogic.androidgames.framework.math.Vector2


open class DynamicGameObject(x: Float, y: Float, width: Float, height: Float) :
    GameObject(x, y, width, height) {

    val velocity = Vector2()
    val accel = Vector2()
}