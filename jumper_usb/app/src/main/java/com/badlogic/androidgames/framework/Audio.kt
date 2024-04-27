package com.badlogic.androidgames.framework

interface Audio {
    fun newMusic(fileName : String) : Music
    fun newSound(fileName: String) : Sound

}