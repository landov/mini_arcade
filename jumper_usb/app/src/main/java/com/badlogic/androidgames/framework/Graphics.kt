package com.badlogic.androidgames.framework

interface Graphics {

    enum class PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    fun newPixmap(fileName: String, format: PixmapFormat) : Pixmap

    fun clear(color: Int)

    fun drawPixel(x: Int, y: Int, color: Int)

    fun drawLine(x: Int, y: Int, x2: Int, y2: Int, color: Int)

    fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Int)

    fun drawPixmap(pixmap: Pixmap, x: Int,y: Int);

    fun draxPixmap(
        pixmap: Pixmap,
        x: Int,
        y: Int,
        srcX: Int,
        srcY: Int,
        srcWidth: Int,
        secHeight: Int
    );



    fun getWidth(): Int
    fun getHeight(): Int
}