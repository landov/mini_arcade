package com.badlogic.androidgames.framework.impl

import android.media.SoundPool
import com.badlogic.androidgames.framework.Sound

class AndroidSound(private val soundPool: SoundPool, private val soundId: Int) : Sound {
    override fun play(volume: Float) {
        soundPool.play(soundId, volume, volume, 0, 0, 1f)
    }

    override fun dispose() {
        soundPool.unload(soundId)
    }

}