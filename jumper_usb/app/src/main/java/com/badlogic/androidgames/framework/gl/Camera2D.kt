package com.badlogic.androidgames.framework.gl

import com.badlogic.androidgames.framework.impl.GLGraphics
import com.badlogic.androidgames.framework.math.Vector2
import javax.microedition.khronos.opengles.GL10

class Camera2D(val glGraphics: GLGraphics, val frustumWidth: Float, val frustumHeight: Float) {
    val position = Vector2(frustumWidth / 2, frustumHeight / 2)
    var zoom = 1f

    fun setViewportAndMatrices() {
        val gl = glGraphics.gl!!
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight())
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        gl.glOrthof(
            position.x - frustumWidth * zoom / 2,
            position.x + frustumWidth * zoom / 2,
            position.y - frustumHeight * zoom / 2,
            position.y + frustumHeight * zoom / 2,
            1f, -1f
        )
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    fun touchToWorld(touch: Vector2) {
        touch.x = (touch.x / glGraphics.getWidth().toFloat()) * frustumWidth * zoom
        touch.y = (1 - touch.y / glGraphics.getHeight().toFloat()) * frustumHeight * zoom
        touch.add(position).sub(frustumWidth * zoom /2, frustumHeight * zoom /2)
    }
}