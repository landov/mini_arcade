package com.badlogic.androidgames.framework

abstract class Screen(protected val game: Game) {

    abstract fun update(deltaTime: Float)
    abstract fun present(deltaTime: Float)
    abstract fun pause()
    abstract fun resume()
    abstract fun dispose()



}