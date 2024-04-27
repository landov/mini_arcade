package com.badlogic.androidgames.framework.impl

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.badlogic.androidgames.framework.Graphics
import com.badlogic.androidgames.framework.Pixmap
import java.io.IOException
import java.io.InputStream

class AndroidGraphics(private val assets: AssetManager, private val frameBuffer: Bitmap) :
    Graphics {
    private val canvas = Canvas(frameBuffer)
    private val paint = Paint()
    private val srcRect = Rect()
    private val dstRect = Rect()
    override fun newPixmap(fileName: String, format: Graphics.PixmapFormat): Pixmap {
        var format = format
        var config: Config? = null
        if (format == Graphics.PixmapFormat.RGB565)
            config = Config.RGB_565
        else if (format == Graphics.PixmapFormat.ARGB4444)
            config = Config.ARGB_4444
        else config = Config.ARGB_8888

        val options = Options()
        options.inPreferredConfig = config

        var inStream: InputStream? = null
        var bitmap: Bitmap? = null
        try {
            inStream = assets.open(fileName)
            bitmap = BitmapFactory.decodeStream(inStream)
            if (bitmap == null)
                throw RuntimeException("Couldn't load bitmap from asset '$fileName'")
        } catch (e: IOException) {
            throw RuntimeException("Couldn't load bitmap from asset '$fileName'")
        } finally {
            inStream?.let {
                try {
                    inStream.close()
                } catch (_: IOException) {
                }
            }
        }
        if (bitmap?.getConfig() == Config.RGB_565)
            format = Graphics.PixmapFormat.RGB565;
        else if (bitmap?.getConfig() == Config.ARGB_4444)
            format = Graphics.PixmapFormat.ARGB4444;
        else
            format = Graphics.PixmapFormat.ARGB8888;

        return AndroidPixmap(bitmap!!, format);

    }

    override fun clear(color: Int) {
        canvas.drawRGB((color and 0xff0000) shr 16, (color and 0xff00) shr 8, (color and 0xff))
    }

    override fun drawPixel(x: Int, y: Int, color: Int) {
        paint.color = color
        canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
    }

    override fun drawLine(x: Int, y: Int, x2: Int, y2: Int, color: Int) {
        paint.color = color
        canvas.drawLine(x.toFloat(), y.toFloat(), x2.toFloat(), y2.toFloat(), paint)
    }

    override fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Int) {
        val x = x.toFloat()
        val y = y.toFloat()
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint)
    }

    override fun draxPixmap(
        pixmap: Pixmap,
        x: Int,
        y: Int,
        srcX: Int,
        srcY: Int,
        srcWidth: Int,
        secHeight: Int,
    ) {
        srcRect.apply {
            left = srcX
            top = srcY
            right = srcX + srcWidth - 1
            bottom = srcY + secHeight - 1
        }

        dstRect.apply {
            left = x
            top = y
            right = x + srcWidth - 1
            bottom = y + secHeight - 1
        }

        canvas.drawBitmap((pixmap as AndroidPixmap).bitmap,srcRect,dstRect,null)
    }

    override fun drawPixmap(pixmap: Pixmap, x: Int, y: Int) {
        canvas.drawBitmap((pixmap as AndroidPixmap).bitmap,x.toFloat(),y.toFloat(),null)
    }

    override fun getWidth(): Int = frameBuffer.width

    override fun getHeight(): Int = frameBuffer.height
}