package com.badlogic.androidgames.framework


interface Music {
    fun play()
    fun stop()
    fun pause()
    fun setLooping(isLooping: Boolean)
    fun setVolume(volume: Float)
    fun isPlaying(): Boolean
    fun isStopped(): Boolean
    fun isLooping(): Boolean
    fun dispose()
}