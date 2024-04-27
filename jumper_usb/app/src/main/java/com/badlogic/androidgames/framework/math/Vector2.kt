package com.badlogic.androidgames.framework.math

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class Vector2(var x: Float, var y: Float) {

    constructor() : this(0f,0f)
    constructor(other: Vector2) : this(other.x, other.y)

    fun copy(): Vector2 = Vector2(x, y)

    fun set(x: Float, y: Float): Vector2 {
        this.x = x
        this.y = y
        return this
    }

    fun set(other: Vector2): Vector2 {
        this.x = other.x
        this.y = other.y
        return this
    }

    fun add(x: Float, y: Float) : Vector2 {
        this.x += x
        this.y += y
        return this
    }

    fun add(other: Vector2) : Vector2 {
        this.x += other.x
        this.y += other.y
        return this
    }

    fun sub(x: Float, y : Float) : Vector2 {
        this.x -= x
        this.y -= y
        return this
    }

    fun sub(other: Vector2) : Vector2 {
        this.x -= other.x
        this.y -= other.y
        return this
    }

    fun mul(scalar : Float) : Vector2 {
        this.x *= scalar
        this.y *= scalar
        return this
    }

    fun len() : Float{
        return sqrt(x * x + y * y)
    }

    fun nor() : Vector2{
        val len = len()
        if(len != 0f){
            this.x /= len
            this.y /= len
        }
        return this
    }

    fun angle() : Float {
        var angle = atan2(y,x) * TO_DEGREES
        if(angle < 0) angle += 360
        return angle.toFloat()
    }

    fun rotate(angle : Float) : Vector2{
        val rad = angle * TO_RADIANS
        val cos = cos(rad.toDouble()).toFloat()
        val sin = sin(rad.toDouble()).toFloat()
        val newX = this.x * cos - this.y * sin
        val newY = this.x * sin + this.y * cos
        this.x = newX
        this.y = newY

        return this
    }

    fun dist(other: Vector2) : Float{
        val distX = this.x - other.x
        val distY = this.y - other.y
        return sqrt(distX*distX+distY*distY)
    }

    fun distSquared(other: Vector2) : Float {
        val distX = this.x - other.x
        val distY = this.y - other.y
        return distX*distX+distY*distY
    }

    fun distSquared(x: Float, y: Float) : Float{
        val distX = this.x - x
        val distY = this.y - y
        return distX * distX + distY * distY
    }

    companion object {

        const val TO_RADIANS = (1 / 180f) * PI.toFloat()
        const val TO_DEGREES = (1 / PI.toFloat()) * 180
    }
}