package com.badlogic.androidgames.framework.impl

import android.app.Activity
import android.media.SoundPool
import com.badlogic.androidgames.framework.Audio
import com.badlogic.androidgames.framework.Music
import com.badlogic.androidgames.framework.Sound
import java.io.IOException

class AndroidAudio(private val activity: Activity) : Audio {
    private val assets = activity.assets
    private val soundPool = SoundPool.Builder().setMaxStreams(20).build()

    override fun newMusic(fileName: String): Music {
        try {
            val assetDescriptor = assets.openFd(fileName)
            return AndroidMusic(assetDescriptor)
        } catch (e: IOException) {
            throw RuntimeException("Couldn't load music '${fileName}'")
        }
    }

    override fun newSound(fileName: String) : Sound {
        try {
            val assetDescriptor = assets.openFd(fileName)
            val sounId = soundPool.load(assetDescriptor, 0)
            return AndroidSound(soundPool, sounId)
        } catch (e: IOException) {
            throw RuntimeException("Couldn't load sound '${fileName}'")
        }
    }
}