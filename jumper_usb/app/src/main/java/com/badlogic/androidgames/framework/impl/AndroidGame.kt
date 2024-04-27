package com.badlogic.androidgames.framework.impl

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.util.Half.toFloat
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import com.badlogic.androidgames.framework.Audio
import com.badlogic.androidgames.framework.FileIO
import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Graphics
import com.badlogic.androidgames.framework.Input
import com.badlogic.androidgames.framework.Screen

abstract class AndroidGame : Activity(), Game {
    private lateinit var renderView: AndroidFastRenderView
    private lateinit var graphics: Graphics
    private lateinit var audio: Audio
    private lateinit var input: Input
    private lateinit var fileIO: FileIO
    private lateinit var screen: Screen

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
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val framBufferWidth = if(isLandscape) 480 else 320
        val framBufferHeight = if(isLandscape) 320 else 480
        val frameBuffer = Bitmap.createBitmap(framBufferWidth,framBufferHeight,Bitmap.Config.RGB_565)
        //TODO implement as above (depreciation)

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }
        val scaleX : Float = framBufferWidth /outMetrics.widthPixels.toFloat()
        val scaleY : Float = framBufferHeight / outMetrics.heightPixels.toFloat()
        Log.d("SCALEx",scaleX.toString())
        Log.d("SCALEy",scaleX.toString())
        //val scaleX : Float = framBufferWidth / windowManager.defaultDisplay.width.toFloat()
        //val scaleY : Float = framBufferHeight / windowManager.defaultDisplay.height.toFloat()
        renderView = AndroidFastRenderView(this,frameBuffer)
        graphics = AndroidGraphics(assets,frameBuffer)
        fileIO = AndroidFileIO(this)
        audio = AndroidAudio(this)
        input = AndroidInput(this,renderView,scaleX,scaleY)
        screen = getStartScreen()
        setContentView(renderView)
    }

    override fun onResume(){
        super.onResume()
        screen.resume()
        renderView.resume()
    }

    override fun onPause() {
        renderView.pause()
        screen.pause()
        if(isFinishing){
            screen.dispose()
        }
        super.onPause()
    }

    override fun getInput(): Input {
      return input
    }

    override fun getFileIO(): FileIO = fileIO

    override fun getGraphics(): Graphics = graphics

    override fun getAudio(): Audio = audio

    override fun setScreen(screen: Screen) {
        this.screen.pause()
        this.screen.dispose()
        screen.resume()
        screen.update(0f)
        this.screen = screen
    }

    override fun getCurrentScreen(): Screen = screen
}