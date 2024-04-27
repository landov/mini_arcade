package com.badlogic.androidgames.framework.gl

class Font(
    val texture: Texture, val offsetX: Int, val offsetY: Int,
    val glyphsPerRow: Int, val glyphWidth: Int, val glyphHeight: Int,
) {

    val glyphs = makeGlyphs()

    private fun makeGlyphs(): Array<TextureRegion> {
        val glyphList: MutableList<TextureRegion> = mutableListOf()
        var x = offsetX
        var y = offsetY
        for (i in 0..<96) {
            glyphList.add(
                TextureRegion(
                    texture,
                    x.toFloat(),
                    y.toFloat(),
                    glyphWidth.toFloat(),
                    glyphHeight.toFloat()
                )
            )
            x += glyphWidth
            if (x == offsetX + glyphsPerRow * glyphWidth) {
                x = offsetX
                y += glyphHeight
            }
        }
        return glyphList.toTypedArray()
    }

    fun drawText(batcher: SpriteBatcher, text: String, x: Float, y: Float) {
        var myX = x
        text.forEach { car ->
            val code = car - ' '
            if(code < 0 || code > glyphs.size - 1) return@forEach
            val glyph = glyphs[code]
            batcher.drawSprite(myX,y,glyphWidth.toFloat(),glyphHeight.toFloat(),glyph)
            myX += glyphWidth
        }
    }
}