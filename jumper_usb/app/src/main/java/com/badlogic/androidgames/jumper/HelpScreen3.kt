package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Input
import com.badlogic.androidgames.framework.gl.Camera2D
import com.badlogic.androidgames.framework.gl.SpriteBatcher
import com.badlogic.androidgames.framework.gl.Texture
import com.badlogic.androidgames.framework.gl.TextureRegion
import com.badlogic.androidgames.framework.impl.GLScreen
import com.badlogic.androidgames.framework.math.OverlapTester
import com.badlogic.androidgames.framework.math.Rectangle
import com.badlogic.androidgames.framework.math.Vector2
import javax.microedition.khronos.opengles.GL10

class HelpScreen3(game: Game) : GLScreen(game) {

    val guiCam = Camera2D (glGraphics,320f,480f)
    val nextBound = Rectangle (320f-64,0f,64f,64f)
    val touchPoint = Vector2()
    val batcher = SpriteBatcher(glGraphics,1)
    var helpImage = Texture(glGame,"help3.png")
    var helpRegion = TextureRegion(helpImage,0f,0f,320f,480f)

    override fun update(deltaTime: Float) {
        val touchEvents = game.getInput().getTouchEvents()
        game.getInput().getKeyEvents()

        touchEvents.forEach {event ->
            touchPoint.set(event.x.toFloat(),event.y.toFloat())
            guiCam.touchToWorld(touchPoint)
            if(event.type == Input.TouchEvent.TOUCH_UP){
                if(OverlapTester.pointInRectangle(nextBound,touchPoint)){
                    Assets.playSound(Assets.clickSound)
                    game.setScreen(HelpScreen4(game))
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

        batcher.beginBatch(helpImage)
        batcher.drawSprite(160f,240f,320f,480f,helpRegion)
        batcher.endBatch()

        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA)

        batcher.beginBatch(Assets.items)
        batcher.drawSprite(320f-32,32f,-64f,64f,Assets.arrow)
        batcher.endBatch()
        gl.glDisable(GL10.GL_BLEND)
    }

    override fun pause() {
        helpImage.dispose()
    }

    override fun resume() {
        helpImage = Texture(glGame,"help3.png")
        helpRegion = TextureRegion(helpImage,0f,0f,320f,480f)
    }

    override fun dispose() {
    }
}