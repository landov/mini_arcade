package com.badlogic.androidgames.framework

interface Pixmap {

    fun getWidth(): Int
    fun getHeight(): Int
    fun getFormat() : Graphics.PixmapFormat
    fun dispose()
}