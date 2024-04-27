package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Input
import com.badlogic.androidgames.framework.gl.Camera2D
import com.badlogic.androidgames.framework.gl.SpriteBatcher
import com.badlogic.androidgames.framework.impl.GLScreen
import com.badlogic.androidgames.framework.math.OverlapTester
import com.badlogic.androidgames.framework.math.Rectangle
import com.badlogic.androidgames.framework.math.Vector2
import java.lang.Integer.max
import javax.microedition.khronos.opengles.GL10

class HighScoresScreen(game: Game) : GLScreen(game) {
    val guiCam = Camera2D (glGraphics,320f,480f)
    val backBound = Rectangle (0f,0f,64f,64f)
    val touchPoint = Vector2()
    val batcher = SpriteBatcher(glGraphics,100)
    val highScores = Array<String>(5,{i -> "${i+1} ${Settings.highscores[i]}"})
    var xOffset = 0

    init{
        highScores.forEach { score ->
            xOffset = max(score.length * Assets.font.glyphWidth, xOffset)
        }
        xOffset = 160 - xOffset / 2
    }

    override fun update(deltaTime: Float) {
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()

        touchEvents.forEach {event ->
            touchPoint.set(event.x.toFloat(),event.y.toFloat())
            guiCam.touchToWorld(touchPoint)
            if(event.type == Input.TouchEvent.TOUCH_UP){
                if(OverlapTester.pointInRectangle(backBound,touchPoint)){
                    Assets.playSound(Assets.clickSound)
                    game.setScreen(MainMenuScreen(game))
                    return
                }
            }

        }
    }

    override fun present(deltaTime: Float) {
        val gl = glGraphics.gl!!
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
        guiCam.setViewportAndMatrices()
        gl.glEnable(GL10.GL_TEXTURE_2D)

        batcher.beginBatch(Assets.background)
        batcher.drawSprite(160f,240f,320f,480f,Assets.backgroundRegion)
        batcher.endBatch()

        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA)

        batcher.beginBatch(Assets.items)
        batcher.drawSprite(160f,360f,300f,33f,Assets.highScoreRegion)
        batcher.endBatch()
        var y = 240f
        for(i in 4 downTo 0){
            Assets.font.drawText(batcher,highScores[i],xOffset.toFloat(),y)
            y += Assets.font.glyphHeight
        }
        batcher.drawSprite(32f,32f,64f,64f,Assets.arrow)
        batcher.endBatch()
        gl.glDisable(GL10.GL_BLEND)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun dispose() {

    }
}