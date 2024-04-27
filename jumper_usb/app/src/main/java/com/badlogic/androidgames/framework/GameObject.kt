package com.badlogic.androidgames.framework

import com.badlogic.androidgames.framework.math.Rectangle
import com.badlogic.androidgames.framework.math.Vector2

open class GameObject(x: Float, y: Float, width: Float, height: Float) {
    val position = Vector2(x,y)
    val bounds = Rectangle(x-width/2,y-height/2, width, height)
}