package com.badlogic.androidgames.framework.impl

import com.badlogic.androidgames.framework.Game
import com.badlogic.androidgames.framework.Screen

abstract class GLScreen(game : Game) : Screen(game) {

    val glGame = (game as GLGame)
    val glGraphics = glGame.getGLGraphics()
}