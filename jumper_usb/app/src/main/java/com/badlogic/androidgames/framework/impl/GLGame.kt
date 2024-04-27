package com.badlogic.androidgames.framework.impl

import android.app.Activity
import android.opengl.GLSurfaceView
import android.opengl.GLSurfaceView.Renderer
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import com.badlogic.androidgames.framework.Audio
import com.badlogic.androidgames.framework.FileIO
import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Graphics
import com.badlogic.androidgames.framework.Input
import com.badlogic.androidgames.framework.Screen
import java.lang.IllegalStateException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class GLGame : Activity(), Game, Renderer {

    enum class GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    private lateinit var glView: GLSurfaceView
    private lateinit var glGraphics: GLGraphics
    private lateinit var audio: Audio
    private lateinit var input: Input
    private lateinit var fileIO: FileIO
    private lateinit var screen: Screen
    private var state = GLGameState.Initialized
    var stateChanged = Object()
    private var startTime = System.nanoTime()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        glView = GLSurfaceView(this)
        glView.setRenderer(this)
        setContentView(glView)
        glGraphics = GLGraphics(glView)
        fileIO = AndroidFileIO(this)
        audio = AndroidAudio(this)
        input = AndroidInput(this,glView,1f,1f)
    }

    override fun onResume() {
        super.onResume()
        glView.onResume()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glGraphics.setGL(gl)

        synchronized(stateChanged){
            if(state == GLGameState.Initialized) screen = getStartScreen()
            state = GLGameState.Running
            screen.resume()
            startTime = System.nanoTime()
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    }

    override fun onDrawFrame(gl: GL10) {
        lateinit var myState : GLGameState
        synchronized(stateChanged){
            myState = this.state
        }

        if(myState == GLGameState.Running){
            val deltaTime = (System.nanoTime()-startTime) / 1_000_000_000.0f
            startTime = System.nanoTime()
            screen.update(deltaTime)
            screen.present(deltaTime)
        }

        if(myState == GLGameState.Paused){
            screen.pause()
            synchronized(stateChanged){
                this.state = GLGameState.Idle
                stateChanged.notifyAll()
            }
        }

        if(myState == GLGameState.Finished){
            screen.pause()
            screen.dispose()
            synchronized(stateChanged){
                this.state = GLGameState.Idle
                stateChanged.notifyAll()
            }
        }
    }

    override fun onPause() {
        synchronized(stateChanged){
            if(isFinishing()) state = GLGameState.Finished
            else state = GLGameState.Paused
            while(true){
                try{
                    stateChanged.wait()
                    break
                } catch (_ : InterruptedException){}
            }
        }
        glView.onPause()
        super.onPause()
    }

    fun getGLGraphics() : GLGraphics{
        return  glGraphics
    }

    override fun getInput(): Input {
        return input
    }

    override fun getFileIO(): FileIO {
        return fileIO
    }

    override fun getGraphics(): Graphics {
        throw  IllegalStateException("We are using OpenGl!")
    }

    override fun getAudio(): Audio {
        return audio
    }

    override fun setScreen(newScreen: Screen) {
        this.screen.pause()
        this.screen.dispose()
        newScreen.resume()
        newScreen.update(0f)
        this.screen = newScreen

    }

    override fun getCurrentScreen(): Screen {
        return screen
    }

}