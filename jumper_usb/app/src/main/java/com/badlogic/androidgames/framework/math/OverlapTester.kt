package com.badlogic.androidgames.framework.math

object OverlapTester {

    fun overlapCircles(c1: Circle, c2: Circle): Boolean {
        val distance = c1.center.distSquared(c2.center)
        val radiusSum = c1.radius + c2.radius
        return distance < radiusSum * radiusSum
    }

    fun overlapRectangles(r1: Rectangle, r2: Rectangle): Boolean {
        return r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&
                r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
                r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
                r1.lowerLeft.y + r1.height > r2.lowerLeft.y
    }

    fun overlapCircleRectangle(c: Circle, r: Rectangle): Boolean {
        var closestX = c.center.x
        var closestY = c.center.y

        if (c.center.x < r.lowerLeft.x) {
            closestX = r.lowerLeft.x
        } else if (c.center.x > r.lowerLeft.x + r.width) {
            closestX = r.lowerLeft.x + r.width
        }

        if(c.center.y < r.lowerLeft.y){
            closestY = r.lowerLeft.y
        } else if (c.center.y > r.lowerLeft.y + r.height){
            closestY = r.lowerLeft.y + r.height
        }

        return  c.center.distSquared(closestX, closestY) < c.radius * c.radius

    }

    fun pointInCircle(c: Circle, p: Vector2?): Boolean {
        return c.center.distSquared(p!!) < c.radius * c.radius
    }

    fun pointInCircle(c: Circle, x: Float, y: Float): Boolean {
        return c.center.distSquared(x, y) < c.radius * c.radius
    }

    fun pointInRectangle(r: Rectangle, p: Vector2): Boolean {
        return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x && r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y
    }

    fun pointInRectangle(r: Rectangle, x: Float, y: Float): Boolean {
        return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x && r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y
    }

}