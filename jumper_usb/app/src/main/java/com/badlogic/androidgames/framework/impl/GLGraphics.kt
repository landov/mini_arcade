package com.badlogic.androidgames.framework.impl

import android.opengl.GLSurfaceView
import javax.microedition.khronos.opengles.GL10

class GLGraphics(val glView : GLSurfaceView) {

    private var _gl : GL10? = null
    val gl : GL10?
        get() = _gl

    fun setGL(gl : GL10){
        _gl = gl
    }

    fun getWidth() : Int =  glView.width
    fun getHeight() : Int = glView.height


}