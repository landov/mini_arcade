package com.badlogic.androidgames.jumper

import android.util.Log
import com.badlogic.androidgames.framework.DynamicGameObject



class Bob(x: Float,y: Float) : DynamicGameObject(x,y, BOB_WIDTH, BOB_HEIGHT) {
    var state = BOB_STATE_FALL
    var stateTime = 0f

    fun update(deltaTime: Float){
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime)
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)

        if(velocity.y > 0 && state != BOB_STATE_HIT){
            if(state != BOB_STATE_JUMP){
                state = BOB_STATE_JUMP
                stateTime = 0f
            }
        }

        if(velocity.y < 0 && state != BOB_STATE_HIT){
            if(state != BOB_STATE_FALL){
                state = BOB_STATE_FALL
                stateTime = 0f
            }
        }

        if(position.x < 0) position.x = World.WORLD_WIDTH
        if(position.x > World.WORLD_WIDTH) position.x = 0f

        stateTime += deltaTime

        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2)
    }

    fun hitSquirrel(){
       velocity.set(0f,0f)
       state = BOB_STATE_HIT
       stateTime = 0f
    }

    fun hitPlatform(){
        velocity.y = BOB_JUMP_VELOCITY
        state = BOB_STATE_JUMP
        stateTime = 0f
    }

    fun hitSpring(){
        velocity.y = BOB_JUMP_VELOCITY * 1.5f
        state = BOB_STATE_JUMP
        stateTime = 0f
    }

    companion object{
        const val BOB_STATE_JUMP = 0
        const val BOB_STATE_FALL = 1
        const val BOB_STATE_HIT = 2
        const val BOB_JUMP_VELOCITY = 11f
        const val BOB_MOVE_VELOCITY = 20f
        const val BOB_WIDTH = 0.8f
        const val BOB_HEIGHT = 0.8f
    }

}