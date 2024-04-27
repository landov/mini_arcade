package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.GameObject



class Spring(x :Float,y:Float) : GameObject(x,y, SPRING_WIDTH, Spring_HEIGHT) {

    companion object{
        const val SPRING_WIDTH = 0.3f
        const val Spring_HEIGHT = 0.3f
    }
}