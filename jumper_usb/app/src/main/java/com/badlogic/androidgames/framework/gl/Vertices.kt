package com.badlogic.androidgames.framework.gl

import com.badlogic.androidgames.framework.impl.GLGraphics
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10

class Vertices(
    val glGraphics: GLGraphics,
    maxVertices: Int,
    maxIndices: Int,
    val hasColor: Boolean,
    val hasTexCoords: Boolean,
) {
    val vertexSize: Int = (2 + (if (hasColor) 4 else 0) + if (hasTexCoords) 2 else 0) * 4
    val vertices: FloatBuffer
    val indices: ShortBuffer?

    init {
        var buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize)
        buffer.order(ByteOrder.nativeOrder())
        vertices = buffer.asFloatBuffer()
        if (maxIndices > 0) {
            buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE_BYTES)
            buffer.order(ByteOrder.nativeOrder())
            indices = buffer.asShortBuffer()
        } else {
            indices = null
        }
    }

    fun setVertices(vertices: FloatArray, offset: Int, length: Int){
        this.vertices.clear()
        this.vertices.put(vertices, offset, length)
        this.vertices.flip()
    }

    fun setIndices(indices: ShortArray, offset: Int, length: Int){
        if(this.indices == null) throw NullPointerException("Not indexed Vertices.")
        this.indices.clear()
        this.indices.put(indices,offset,length)
        this.indices.flip()
    }

    fun bind(){
        val gl = glGraphics.gl!!
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        vertices.position(0)
        gl.glVertexPointer(2,GL10.GL_FLOAT,vertexSize,vertices)

        if(hasColor){
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
            vertices.position(2)
            gl.glColorPointer(4, GL10.GL_FLOAT,vertexSize,vertices)
        }

        if(hasTexCoords){
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
            vertices.position(if(hasColor) 6 else 2)
            gl.glTexCoordPointer(2,GL10.GL_FLOAT,vertexSize,vertices)
        }
    }

    fun draw(primitiveType: Int, offset: Int, numVertices: Int){
        val gl = glGraphics.gl!!
        if(indices != null){
            indices.position(offset)
            gl.glDrawElements(primitiveType,numVertices,GL10.GL_UNSIGNED_SHORT,indices)
        } else {
            gl.glDrawArrays(primitiveType,offset,numVertices)
        }
    }

    fun unBind(){
        val gl = glGraphics.gl!!
        if(hasTexCoords) gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        if(hasColor) gl.glDisableClientState(GL10.GL_COLOR_ARRAY)

    }
}