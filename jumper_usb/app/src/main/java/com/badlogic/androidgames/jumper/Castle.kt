package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.GameObject



class Castle(x: Float, y: Float) : GameObject(x,y,CASTLE_WIDTH, CASTLE_HEIGHT) {
    companion object{
        const val CASTLE_WIDTH = 1.7f
        const val CASTLE_HEIGHT = 1.7f
    }
}