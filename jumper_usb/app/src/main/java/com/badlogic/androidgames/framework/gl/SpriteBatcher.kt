package com.badlogic.androidgames.framework.gl

import com.badlogic.androidgames.framework.impl.GLGraphics
import com.badlogic.androidgames.framework.math.Vector2
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class SpriteBatcher(val glGraphics: GLGraphics, maxSprites: Int) {
    val verticesBuffer = FloatArray(maxSprites * 4 * 4)
    val vertices = Vertices(glGraphics, maxSprites * 4, maxSprites * 6, false, true)
    var bufferIndex = 0
    var numSprites = 0
    val indices = ShortArray(maxSprites * 6)


    init {
        val len = indices.size
        var j: Short = 0
        var i = 0
        while (i < len) {
            indices[i + 0] = (j + 0).toShort()
            indices[i + 1] = (j + 1).toShort()
            indices[i + 2] = (j + 2).toShort()
            indices[i + 3] = (j + 2).toShort()
            indices[i + 4] = (j + 3).toShort()
            indices[i + 5] = (j + 0).toShort()
            i += 6
            j = (j + 4).toShort()
        }
        vertices.setIndices(indices, 0, indices.size)
    }

    fun beginBatch(texture: Texture) {
        texture.bind()
        numSprites = 0
        bufferIndex = 0
    }

    fun endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex)
        vertices.bind()
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6)
        vertices.unBind()
    }

    fun drawSprite(x: Float, y: Float, width: Float, height: Float, region: TextureRegion) {
        val halfWidth = width / 2
        val halfHeight = height / 2
        val x1 = x - halfWidth
        val y1 = y - halfHeight
        val x2 = x + halfWidth
        val y2 = y + halfHeight
        verticesBuffer[bufferIndex++] = x1
        verticesBuffer[bufferIndex++] = y1
        verticesBuffer[bufferIndex++] = region.u1
        verticesBuffer[bufferIndex++] = region.v2

        verticesBuffer[bufferIndex++] = x2
        verticesBuffer[bufferIndex++] = y1
        verticesBuffer[bufferIndex++] = region.u2
        verticesBuffer[bufferIndex++] = region.v2

        verticesBuffer[bufferIndex++] = x2
        verticesBuffer[bufferIndex++] = y2
        verticesBuffer[bufferIndex++] = region.u2
        verticesBuffer[bufferIndex++] = region.v1

        verticesBuffer[bufferIndex++] = x1
        verticesBuffer[bufferIndex++] = y2
        verticesBuffer[bufferIndex++] = region.u1
        verticesBuffer[bufferIndex++] = region.v1

        numSprites++
    }

    fun drawSprite(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        angle: Float,
        region: TextureRegion,
    ) {
        val halfWidth = width / 2
        val halfHeight = height / 2
        val rad = angle * Vector2.TO_RADIANS
        val cos = cos(rad)
        val sin = sin(rad)
        var x1 = -halfWidth * cos - (-halfHeight) * sin
        var y1 = -halfWidth * sin + (-halfHeight) * cos
        var x2 = halfWidth * cos - (-halfHeight) * sin
        var y2 = halfWidth * sin + (-halfHeight) * cos
        var x3 = halfWidth * cos - halfHeight * sin
        var y3 = halfWidth * sin + halfHeight * cos
        var x4 = -halfWidth * cos - halfHeight * sin
        var y4 = -halfWidth * sin + halfHeight * cos

        x1 += x
        y1 += y
        x2 += x
        y2 += y
        x3 += x
        y3 += y
        x4 += x
        y4 += y

        verticesBuffer[bufferIndex++] = x1
        verticesBuffer[bufferIndex++] = y1
        verticesBuffer[bufferIndex++] = region.u1
        verticesBuffer[bufferIndex++] = region.v2

        verticesBuffer[bufferIndex++] = x2
        verticesBuffer[bufferIndex++] = y2
        verticesBuffer[bufferIndex++] = region.u2
        verticesBuffer[bufferIndex++] = region.v2

        verticesBuffer[bufferIndex++] = x3
        verticesBuffer[bufferIndex++] = y3
        verticesBuffer[bufferIndex++] = region.u2
        verticesBuffer[bufferIndex++] = region.v1

        verticesBuffer[bufferIndex++] = x4
        verticesBuffer[bufferIndex++] = y4
        verticesBuffer[bufferIndex++] = region.u1
        verticesBuffer[bufferIndex++] = region.v1

        numSprites++

    }

}