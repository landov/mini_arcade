package com.badlogic.androidgames.jumper

import android.graphics.Rect
import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Input
import com.badlogic.androidgames.framework.Input.TouchEvent
import com.badlogic.androidgames.framework.gl.Camera2D
import com.badlogic.androidgames.framework.gl.SpriteBatcher
import com.badlogic.androidgames.framework.impl.GLGame
import com.badlogic.androidgames.framework.impl.GLScreen
import com.badlogic.androidgames.framework.math.OverlapTester
import com.badlogic.androidgames.framework.math.Rectangle
import com.badlogic.androidgames.framework.math.Vector2
import javax.microedition.khronos.opengles.GL10

class MainMenuScreen(game: Game) : GLScreen(game) {

    val guiCam = Camera2D(glGraphics, 320f, 480f)
    val batcher = SpriteBatcher(glGraphics, 100)
    val soundBounds = Rectangle(0f, 0f, 64f, 64f)
    val playBounds = Rectangle(160f - 150, 200f + 18, 300f, 36f)
    val highScoresBounds = Rectangle(160f - 150, 200f - 18, 300f, 36f)
    val helpBounds = Rectangle(160f - 150, 200f - 18 - 36, 300f, 36f)
    val touchPoint = Vector2()


    override fun update(deltaTime: Float) {
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()

        touchEvents.forEach { event ->
            if (event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x.toFloat(), event.y.toFloat())
                guiCam.touchToWorld(touchPoint)

                if (OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound)
                    game.setScreen(GameScreen(game))
                    return
                }
                if (OverlapTester.pointInRectangle(highScoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound)
                    game.setScreen(HighScoresScreen(game))
                    return
                }
                if (OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound)
                    game.setScreen(HelpScreen(game))
                    return
                }
                if (OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound)
                    Settings.soundEnabled = !Settings.soundEnabled
                    if (Settings.soundEnabled) Assets.music.play()
                    else Assets.music.pause()
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
        batcher.drawSprite(160f, 240f, 320f, 480f, Assets.backgroundRegion)
        batcher.endBatch()

        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

        batcher.beginBatch(Assets.items)
        batcher.drawSprite(160f, 480f - 10 - 71, 274f, 142f, Assets.logo)
        batcher.drawSprite(160f, 200f, 300f, 110f, Assets.mainMenu)
        batcher.drawSprite(
            32f,
            32f,
            64f,
            64f,
            if (Settings.soundEnabled) Assets.soundOn else Assets.soundOff
        )
        batcher.endBatch()

        gl.glDisable(GL10.GL_BLEND)
    }

    override fun pause() {
        Settings.save(game.getFileIO())
    }

    override fun resume() {

    }

    override fun dispose() {

    }

}