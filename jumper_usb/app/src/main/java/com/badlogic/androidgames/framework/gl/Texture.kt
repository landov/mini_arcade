package com.badlogic.androidgames.framework.gl

import android.graphics.BitmapFactory
import android.opengl.GLUtils
import com.badlogic.androidgames.framework.impl.GLGame
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.opengles.GL10

class Texture(val glGame: GLGame, val fileName: String) {
    val glGraphics = glGame.getGLGraphics()
    val fileIO = glGame.getFileIO()
    var textureId = 0
    var minFilter = 0
    var magFilter = 0
    var width = 0
    var height = 0

    init {
        load()
    }

    private fun load() {
        var inS: InputStream? = null
        inS = fileIO.readAsset(fileName)
        try {
            val gl = glGraphics.gl!!
            val textureIds = IntArray(1)
            gl.glGenTextures(1,textureIds,0)
            textureId = textureIds[0]
            inS = fileIO.readAsset(fileName)
            val bitmap = BitmapFactory.decodeStream(inS)
            width = bitmap.width
            height = bitmap.height
            gl.glBindTexture(GL10.GL_TEXTURE_2D,textureId)
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D,0,bitmap,0)
            setFilters(GL10.GL_NEAREST,GL10.GL_NEAREST)
            gl.glBindTexture(GL10.GL_TEXTURE_2D,0)
        } catch (e: IOException) {
            throw RuntimeException("Couldn't load texture $fileName !")
        } finally {
            if (inS != null) {
                try {
                    inS.close()
                } catch (_: IOException) {
                }
            }
        }
    }

    fun reload(){
        load()
        bind()      //TODO ForWhat?
        setFilters(minFilter,magFilter) //TODO Why not constructor params?
    }

    fun setFilters(minFilter: Int, magFilter: Int){
        this.magFilter = magFilter
        this.minFilter = minFilter
        val gl = glGraphics.gl!!
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,minFilter.toFloat())
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,magFilter.toFloat())
    }

    fun bind(){
        val gl = glGraphics.gl!!
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureId)
    }

    fun dispose(){
        val gl =glGraphics.gl!!
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureId)
        val textureIds = intArrayOf(textureId)
        gl.glDeleteTextures(1,textureIds,0)
    }
}