package com.badlogic.androidgames.framework.impl

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import com.badlogic.androidgames.framework.Music
import java.io.IOException
import java.lang.IllegalStateException

class AndroidMusic(private val assetDescriptor: AssetFileDescriptor) : Music, OnCompletionListener {
    private val mediaPlayer = MediaPlayer()
    private var isPrepared = false

    init {
        try {
            mediaPlayer.setDataSource(
                assetDescriptor.fileDescriptor,
                assetDescriptor.startOffset,
                assetDescriptor.length
            )
            mediaPlayer.prepare()
            isPrepared = true
            mediaPlayer.setOnCompletionListener(this)
        } catch (e: Exception) {
            throw RuntimeException("Couldn't load music")
        }
    }

    override fun play() {
        if (mediaPlayer.isPlaying) return
        try{
            synchronized(this){
                if(!isPrepared) mediaPlayer.prepare()
                mediaPlayer.start()
            }
        } catch (e: IllegalStateException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        synchronized(this){
            isPrepared = false
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }

    override fun setLooping(isLooping: Boolean) {
        mediaPlayer.isLooping = isLooping
    }

    override fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun isStopped(): Boolean {
        return !isPrepared
    }

    override fun isLooping(): Boolean {
        return mediaPlayer.isLooping
    }

    override fun dispose() {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onCompletion(player: MediaPlayer) {
        synchronized(this){
            isPrepared = false
        }
    }


}