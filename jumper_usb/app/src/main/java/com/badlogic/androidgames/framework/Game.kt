package com.badlogic.androidgames.framework

interface Game {

    fun getInput() : Input
    fun getFileIO(): FileIO
    fun getGraphics(): Graphics
    fun getAudio(): Audio

    fun setScreen(screen: Screen)
    fun getCurrentScreen() : Screen
    fun getStartScreen(): Screen


}