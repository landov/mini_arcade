package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Input
import com.badlogic.androidgames.framework.Input.TouchEvent
import com.badlogic.androidgames.framework.gl.Camera2D
import com.badlogic.androidgames.framework.gl.SpriteBatcher
import com.badlogic.androidgames.framework.impl.AndroidInput
import com.badlogic.androidgames.framework.impl.GLScreen
import com.badlogic.androidgames.framework.math.OverlapTester
import com.badlogic.androidgames.framework.math.Rectangle
import com.badlogic.androidgames.framework.math.Vector2
import java.lang.Math.abs
import javax.microedition.khronos.opengles.GL10

class GameScreen(game: Game) : GLScreen(game) {

    var state = GAME_READY
    val guiCam = Camera2D(glGraphics,320f,480f)
    val touchPoint = Vector2()
    val batcher = SpriteBatcher(glGraphics, 1000)
    val worldListener  = object : World.WordListener {
        override fun jump() {
            Assets.playSound(Assets.jumpSound)
        }
        override fun highJump() {
            Assets.playSound(Assets.highJumpSound)
        }
        override fun hit() {
            Assets.playSound((Assets.hitSound))
        }
        override fun coin() {
            Assets.playSound(Assets.coinSound)
        }
    }
    var world = World(worldListener)
    var renderer = WorldRenderer(glGraphics,batcher,world)
    val pauseBounds = Rectangle(320 -64f,480 -64f,64f,64f)
    val resumeBounds = Rectangle(160- 96f,240f,192f,36f)
    val quitBounds = Rectangle(160-96f,240-36f,192f,36f)
    var lastScore = 0;
    var scoreString = "score: 0"

    override fun update(deltaTime: Float) {
        //Consider removal
       /* var deltaTime = deltaTime
        if(deltaTime > 0.1f) deltaTime = 0.1f*/
        when(state){
            GAME_READY -> updateReady()
            GAME_RUNNING -> updateRunning(deltaTime)
            GAME_PAUSED -> updatePaused()
            GAME_LEVEL_END -> updateLevelEnd()
            GAME_OVER -> updateGameOver()
        }
    }

    private fun updateReady(){
        if(game.getInput().getTouchEvents().isNotEmpty())
            state = GAME_RUNNING
    }

    private fun updateRunning(deltaTime: Float){
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()
        touchEvents.forEach { event ->
            if(event.type == TouchEvent.TOUCH_UP){
                touchPoint.set(event.x.toFloat(),event.y.toFloat())
                guiCam.touchToWorld(touchPoint)
                if(OverlapTester.pointInRectangle(pauseBounds,touchPoint)){
                    Assets.playSound(Assets.clickSound)
                    state = GAME_PAUSED
                    return
                }
            }
        }

        val usbFloat = (game as SuperJumper).control
        //world.update(deltaTime,game.getInput().getAccelX())
        world.update(deltaTime,usbFloat)
        if(world.score != lastScore){
            lastScore = world.score
            scoreString = "$lastScore"
        }
        if(world.state == World.WORLD_STATE_NEXT_LEVEL) state = GAME_LEVEL_END
        if(world.state == World.WORLD_STATE_GAME_OVER){
            state = GAME_OVER
            if(lastScore >= Settings.highscores[4])
                scoreString = "new highScore: $lastScore"
            else
                scoreString = "score: $lastScore"
            Settings.addScore(lastScore)
            Settings.save(game.getFileIO())
        }
    }

    private fun updatePaused(){
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()
        touchEvents.forEach { event ->
            if(event.type == TouchEvent.TOUCH_UP){
                touchPoint.set(event.x.toFloat(),event.y.toFloat())
                guiCam.touchToWorld(touchPoint)

                if(OverlapTester.pointInRectangle(resumeBounds,touchPoint)){
                    Assets.playSound(Assets.clickSound)
                    state = GAME_RUNNING
                    return
                }
                if(OverlapTester.pointInRectangle(quitBounds, touchPoint)){
                    Assets.playSound(Assets.clickSound)
                    game.setScreen(MainMenuScreen(game))
                    return
                }
            }
        }
    }

    private fun updateLevelEnd(){
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()
        touchEvents.forEach { event ->
            if(event.type == TouchEvent.TOUCH_UP){
                world = World(worldListener)
                renderer = WorldRenderer(glGraphics,batcher,world)
                world.score = lastScore
                state = GAME_READY
            }
        }
    }

    private fun updateGameOver(){
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()
        touchEvents.forEach { event ->
            if (event.type == TouchEvent.TOUCH_UP) {
                game.setScreen(MainMenuScreen(game))
                return
            }
        }
    }

    override fun present(deltaTime: Float) {
        val gl = glGraphics.gl!!
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
        gl.glEnable(GL10.GL_TEXTURE_2D)

        renderer.render()

        guiCam.setViewportAndMatrices()
        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA)
        batcher.beginBatch(Assets.items)
        when(state){
            GAME_READY -> presentReady()
            GAME_RUNNING -> presentRunning()
            GAME_PAUSED -> presentPaused()
            GAME_LEVEL_END -> presentLevelEnd()
            GAME_OVER -> presentGameOver()
        }
        batcher.endBatch()
        gl.glDisable(GL10.GL_BLEND)
    }

    private fun presentReady(){
        batcher.drawSprite(160f,240f,192f,32f,Assets.ready)
    }

    private fun presentRunning(){
        batcher.drawSprite(320f-32f,480f-32f,64f,64f,Assets.pause)
        Assets.font.drawText(batcher,scoreString,16f,480f-20)
    }

    private fun presentPaused(){
        batcher.drawSprite(160f,240f,192f,96f,Assets.pauseMenu)
        Assets.font.drawText(batcher,scoreString,16f,480f-20)
    }

    private fun presentLevelEnd(){
        val topText = "the princess is ...."
        val bottomText = "in another castle"
        val topWidth = Assets.font.glyphWidth * topText.length
        val bottomWidth = Assets.font.glyphWidth * bottomText.length
        Assets.font.drawText(batcher,topText,160f-topWidth/2,480-40f)
        Assets.font.drawText(batcher,bottomText,160f-bottomWidth/2,40f)
    }

    private fun presentGameOver(){
        batcher.drawSprite(160f,240f,160f,96f,Assets.gameOver)
        val scoreWidth = Assets.font.glyphWidth * scoreString.length
        Assets.font.drawText(batcher,scoreString,160f-scoreWidth/2,480-20f)
    }

    override fun pause() {
        if(state == GAME_RUNNING) state = GAME_PAUSED
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    companion object{
        const val GAME_READY = 0
        const val GAME_RUNNING = 1
        const val GAME_PAUSED = 2
        const val GAME_LEVEL_END = 3
        const val GAME_OVER = 4
    }
}