package com.badlogic.androidgames.framework.gl

import android.util.Log

class FPSCounter {
    var startTime = System.nanoTime()
    var frames = 0

    fun logFrame() {
        frames++
        if (System.nanoTime() - startTime >= 1_000_000_000) {
            Log.d("FPSCounter", "fps: " + frames)
            frames = 0
            startTime = System.nanoTime()
        }
    }
}