package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.DynamicGameObject

class Squirrel(x: Float,y: Float): DynamicGameObject(x,y, SQUIRREL_WIDTH, SQUIRREL_HEIGHT) {

    var stateTime = 0f

    init{
        velocity.set(SQUIRREL_VELOCITY,0f)
    }

    fun update(deltaTime: Float){
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)


        if(position.x < SQUIRREL_WIDTH  / 2){
            position.x = SQUIRREL_WIDTH / 2
            velocity.x = SQUIRREL_VELOCITY
        }
        if(position.x > World.WORLD_WIDTH - SQUIRREL_WIDTH/2){
            position.x = World.WORLD_WIDTH - SQUIRREL_WIDTH/2
            velocity.x = -SQUIRREL_VELOCITY
        }
        bounds.lowerLeft.set(position).sub(SQUIRREL_WIDTH/2, SQUIRREL_HEIGHT/2)

        stateTime += deltaTime
    }

    companion object{
        const val SQUIRREL_WIDTH = 1f
        const val SQUIRREL_HEIGHT = 0.6f
        const val SQUIRREL_VELOCITY = 3f
    }
}