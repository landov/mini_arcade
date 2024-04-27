package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.GameObject



class Coin(x : Float, y: Float): GameObject(x,y, COIN_WIDTH, COIN_HEIGHT) {

    var stateTime = 0f

    fun update(deltaTime : Float){
        stateTime += deltaTime
    }

    companion object{
        const val COIN_WIDTH = 0.5f
        const val COIN_HEIGHT = 0.8f
        const val COIN_SCORE = 10
    }
}