package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.DynamicGameObject



class Platform(val type: Int, x: Float, y: Float) :
    DynamicGameObject(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT) {
    var state = PLATFORM_STATE_NORMAL
    var stateTime = 0f

    init {
        if (type == PLATFORM_TYPE_MOVING) velocity.x = PLATFORM_VELOCITY
    }

    fun update(deltaTime: Float) {
        if(type == PLATFORM_TYPE_MOVING){
            position.add(velocity.x * deltaTime,0f)

            if(position.x < PLATFORM_WIDTH / 2){
                velocity.x = -velocity.x
                position.x = PLATFORM_WIDTH /2
            }
            if(position.x > World.WORLD_WIDTH - PLATFORM_WIDTH/2){
                velocity.x = -velocity.x
                position.x = World.WORLD_WIDTH - PLATFORM_WIDTH/2
            }

            bounds.lowerLeft.set(position).sub(PLATFORM_WIDTH/2, PLATFORM_HEIGHT/2)
        }

        stateTime += deltaTime
    }

    fun pulverize(){
        state = PLATFORM_STATE_PULVERIZING
        stateTime = 0f
        velocity.x = 0f
    }

    companion object{
        const val PLATFORM_WIDTH = 2f
        const val PLATFORM_HEIGHT = 0.5f
        const val PLATFORM_TYPE_STATIC = 0
        const val PLATFORM_TYPE_MOVING = 1
        const val PLATFORM_STATE_NORMAL = 0
        const val PLATFORM_STATE_PULVERIZING = 1
        const val PLATFORM_PULVERIZE_TIME = 0.2f * 4
        const val PLATFORM_VELOCITY = 2f
    }

}