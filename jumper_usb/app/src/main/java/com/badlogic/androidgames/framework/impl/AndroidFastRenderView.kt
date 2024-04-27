package com.badlogic.androidgames.framework.impl

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.view.SurfaceView

@SuppressLint("ViewConstructor")
class AndroidFastRenderView(val game: AndroidGame, val frameBuffer: Bitmap) :
    SurfaceView(game), Runnable {

    var renderThread : Thread? = null
    private val holder = getHolder()
    @Volatile
    var running = false

    fun resume(){
        running = true
        renderThread = Thread(this)
        renderThread!!.start()
    }

    override fun run() {
        val dstRect = Rect()
        var startTime = System.nanoTime()
        while (running){
            if(!holder.surface.isValid) continue
            val deltaTime = (System.nanoTime() - startTime) / 1_000_000_000f
            startTime = System.nanoTime()
            game.getCurrentScreen().update(deltaTime)
            game.getCurrentScreen().present(deltaTime)
            val canvas = holder.lockCanvas()
            canvas.getClipBounds(dstRect)
            canvas.drawBitmap(frameBuffer,null,dstRect,null)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun pause(){
        running = false
        while (true){
            try{
                renderThread?.join()
                return
            } catch (_ : InterruptedException){}
        }
    }

}