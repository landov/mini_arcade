package com.badlogic.androidgames.framework.gl



class TextureRegion(texture: Texture, x: Float, y: Float,width: Float, height: Float) {
    val u1 = x / texture.width
    val v1 = y /texture.height
    val u2 = u1 + width / texture.width
    val v2 = v1 + height / texture.height
}