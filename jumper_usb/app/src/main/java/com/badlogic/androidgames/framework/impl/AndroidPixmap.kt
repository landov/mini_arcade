package com.badlogic.androidgames.framework.impl

import android.graphics.Bitmap
import com.badlogic.androidgames.framework.Graphics
import com.badlogic.androidgames.framework.Pixmap

class AndroidPixmap(val bitmap: Bitmap, private val format: Graphics.PixmapFormat) : Pixmap {

    override fun getWidth(): Int = bitmap.width

    override fun getHeight(): Int = bitmap.height

    override fun getFormat(): Graphics.PixmapFormat {
        return format
    }

    override fun dispose() {
        bitmap.recycle()
    }

}