package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.gl.Animation
import com.badlogic.androidgames.framework.gl.Camera2D
import com.badlogic.androidgames.framework.gl.SpriteBatcher
import com.badlogic.androidgames.framework.gl.TextureRegion
import com.badlogic.androidgames.framework.impl.GLGraphics
import com.badlogic.androidgames.jumper.Assets.spring
import javax.microedition.khronos.opengles.GL10

class WorldRenderer(val glGraphics: GLGraphics, val batcher: SpriteBatcher, val world: World) {

    val cam = Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT)

    companion object {
        const val FRUSTUM_WIDTH = 10f
        const val FRUSTUM_HEIGHT = 15f
    }

    fun render() {
        if (world.bob.position.y > cam.position.y) cam.position.y = world.bob.position.y
        cam.setViewportAndMatrices()
        renderBackround()
        renderObjects()
    }

    private fun renderBackround() {
        batcher.beginBatch(Assets.background)
        batcher.drawSprite(cam.position.x, cam.position.y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, Assets.backgroundRegion)
        batcher.endBatch()
    }

    private fun renderObjects() {
        val gl = glGraphics.gl!!
        gl.glEnable(GL10.GL_BLEND)

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)
        batcher.beginBatch(Assets.items)

        renderBob()
        renderPlatforms()
        renderItems()
        renderSquirrels()
        renderCastle()

        batcher.endBatch()
        gl.glDisable(GL10.GL_BLEND)
    }

    private fun renderBob() {
        val keyFrame: TextureRegion =
            when (world.bob.state) {
                Bob.BOB_STATE_FALL -> Assets.bobFall.getKeyFrame(
                    world.bob.stateTime, Animation.ANIMATION_LOOPING
                )

                Bob.BOB_STATE_JUMP -> Assets.bobJump.getKeyFrame(
                    world.bob.stateTime, Animation.ANIMATION_LOOPING
                )

                else -> Assets.bobHit
            }
        val side = if (world.bob.velocity.x < 0) -1f else 1f
        batcher.drawSprite(
            world.bob.position.x,
            world.bob.position.y,
            side * 1, 1f, keyFrame
        )
    }

    private fun renderPlatforms() {
        world.platforms.forEach { platform ->
            val keyFrame =
                if (platform.state == Platform.PLATFORM_STATE_PULVERIZING)
                    Assets.brakingPlatform.getKeyFrame(
                        platform.stateTime,
                        Animation.ANIMATION_NONLOOPING
                    )
                else Assets.platform
            batcher.drawSprite(platform.position.x, platform.position.y, 2f, 0.5f, keyFrame)
        }
    }

    private fun renderItems() {
        world.springs.forEach { spring ->
            batcher.drawSprite(spring.position.x, spring.position.y, 1f, 1f, Assets.spring)
        }
        world.coins.forEach { coin ->
            val keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING)
            batcher.drawSprite(coin.position.x, coin.position.y, 1f, 1f, keyFrame)
        }
    }

    private fun renderSquirrels() {
        world.squirrels.forEach { squirrel ->
            val keyFrame =
                Assets.squirellFly.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING)
            val side = if(squirrel.velocity.x < 0) -1f else 1f
            batcher.drawSprite(squirrel.position.x,squirrel.position.y,side * 1, 1f,keyFrame)
        }
    }

    private fun renderCastle(){
        val castle = world.castle
        batcher.drawSprite(castle.position.x,castle.position.y,2f,2f,Assets.castle)
    }
}