package com.badlogic.androidgames.jumper

import android.util.Log
import com.badlogic.androidgames.framework.math.OverlapTester
import com.badlogic.androidgames.framework.math.Vector2
import com.badlogic.androidgames.jumper.Assets.platform
import java.lang.Float.max
import java.util.Random


class World(val listener: WordListener) {

    val bob = Bob(5f, 1f)
    val platforms = ArrayList<Platform>()
    val springs = ArrayList<Spring>()
    val squirrels = ArrayList<Squirrel>()
    val coins = ArrayList<Coin>()
    val rand = Random()
    lateinit var castle : Castle

    var heightSoFar = 0f
    var score = 0
    var state = WORLD_STATE_RUNNING

    init {
        generateLevel()
    }

    interface WordListener {
        fun jump()
        fun highJump()
        fun hit()
        fun coin()
    }

    private fun generateLevel() {
        var y = Platform.PLATFORM_HEIGHT / 2
        val maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * -gravity.y)
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            val type =
                if (rand.nextFloat() > 0.8f) Platform.PLATFORM_TYPE_MOVING else Platform.PLATFORM_TYPE_STATIC
            val x = rand.nextFloat() *
                    (WORLD_WIDTH - Platform.PLATFORM_WIDTH) +
                    Platform.PLATFORM_WIDTH / 2
            val platform = Platform(type, x, y)
            platforms.add(platform)

            if (rand.nextFloat() > 0.9f && type != Platform.PLATFORM_TYPE_MOVING) {
                val spring = Spring(
                    platform.position.x,
                    platform.position.y + Platform.PLATFORM_HEIGHT / 2 +
                            Spring.Spring_HEIGHT / 2
                )
                springs.add(spring)
            }

            if(y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f){
                val squirrel = Squirrel(
                    platform.position.x + rand.nextFloat(),
                    platform.position.y + Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * 2)
                squirrels.add(squirrel)
            }

            if(rand.nextFloat() > 0.6f){
                val coin = Coin(platform.position.x + rand.nextFloat(),
                    platform.position.y + Coin.COIN_HEIGHT + rand.nextFloat() * 3
                    )
                coins.add(coin)
            }

            y += (maxJumpHeight - 0.5f)
            y -= rand.nextFloat() * (maxJumpHeight / 2)
        }

        castle = Castle(WORLD_WIDTH / 2,y)
    }

    fun update(deltaTime: Float, accelX: Float){
        updateBob(deltaTime, accelX)
        updatePlatforms(deltaTime)
        updateSquirrels(deltaTime)
        updateCoins(deltaTime)
        if(bob.state != Bob.BOB_STATE_HIT) checkCollisions()
        checkGameOver()
    }

    private fun updateBob(deltaTime: Float, accelX: Float){
        if(bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f) bob.hitPlatform()
        if(bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = (-accelX / 10) * Bob.BOB_MOVE_VELOCITY
        bob.update(deltaTime)
        heightSoFar = max(bob.position.y, heightSoFar)
    }

    private fun updatePlatforms(deltaTime: Float){
        val removeables = ArrayList<Platform>()
        platforms.forEach {platform ->
        platform.update(deltaTime)
        if(platform.state == Platform.PLATFORM_STATE_PULVERIZING
            && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME)
            //platforms.remove(platform)
            removeables.add(platform)
        }
        removeables.forEach { platform ->
            platforms.remove(platform)
        }
    }

    private fun updateSquirrels(deltaTime: Float){
        squirrels.forEach { squirrel ->
            squirrel.update(deltaTime)
        }
    }

    private fun updateCoins(deltaTime: Float){
        coins.forEach { coin ->
            coin.update(deltaTime)
        }
    }

    private fun checkCollisions(){
        checkPlatformCollisions()
        checkSquirrelCollisions()
        checkItemCollisions()
        checkCastleCollisions()
    }

    private fun checkPlatformCollisions(){
        if(bob.velocity.y > 0) return
        platforms.forEach { platform ->
            if(bob.position.y > platform.position.y){
                if(OverlapTester.overlapRectangles(bob.bounds,platform.bounds)){
                    bob.hitPlatform()
                    listener.jump()
                    if(rand.nextFloat() > 0.5f){
                        platform.pulverize()
                    }
                    return@forEach
                }
            }
        }
    }

    private fun checkSquirrelCollisions(){
        squirrels.forEach { squirrel ->
            if(OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)){
                bob.hitSquirrel()
                listener.hit()
            }
        }
    }

    private fun checkItemCollisions(){
        val removables = ArrayList<Coin>()
        coins.forEach { coin ->
            if(OverlapTester.overlapRectangles(bob.bounds,coin.bounds)){
                //coins.remove(coin)
                removables.add(coin)
                listener.coin()
                score += Coin.COIN_SCORE
            }
        }
        removables.forEach { coin ->
            coins.remove(coin)
        }
        if(bob.velocity.y > 0) return
        springs.forEach { spring ->
            if(bob.position.y > spring.position.y){
                if(OverlapTester.overlapRectangles(bob.bounds,spring.bounds)){
                    bob.hitSpring()
                    listener.highJump()
                }
            }
        }
    }

    private fun checkCastleCollisions(){
        if(OverlapTester.overlapRectangles(castle.bounds,bob.bounds))
            state = WORLD_STATE_NEXT_LEVEL
    }

    private fun checkGameOver(){
        if(heightSoFar - 7.5f > bob.position.y)
            state = WORLD_STATE_GAME_OVER
    }

    companion object {
        const val WORLD_WIDTH = 10f
        const val WORLD_HEIGHT = 15 * 20f
        const val WORLD_STATE_RUNNING = 0
        const val WORLD_STATE_NEXT_LEVEL = 1
        const val WORLD_STATE_GAME_OVER = 2

        val gravity = Vector2(0f, -12f)
    }
}