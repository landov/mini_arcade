package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.Music
import com.badlogic.androidgames.framework.Sound
import com.badlogic.androidgames.framework.gl.Animation
import com.badlogic.androidgames.framework.gl.Font
import com.badlogic.androidgames.framework.gl.Texture
import com.badlogic.androidgames.framework.gl.TextureRegion
import com.badlogic.androidgames.framework.impl.GLGame

object Assets {

    lateinit var background: Texture
    lateinit var backgroundRegion: TextureRegion

    lateinit var items: Texture
    lateinit var mainMenu: TextureRegion
    lateinit var pauseMenu: TextureRegion
    lateinit var ready: TextureRegion
    lateinit var gameOver: TextureRegion
    lateinit var highScoreRegion: TextureRegion
    lateinit var logo: TextureRegion
    lateinit var soundOn: TextureRegion
    lateinit var soundOff: TextureRegion
    lateinit var arrow: TextureRegion
    lateinit var pause: TextureRegion
    lateinit var spring: TextureRegion
    lateinit var castle: TextureRegion
    lateinit var coinAnim: Animation
    lateinit var bobJump: Animation
    lateinit var bobFall: Animation
    lateinit var bobHit: TextureRegion
    lateinit var squirellFly: Animation
    lateinit var platform: TextureRegion
    lateinit var brakingPlatform: Animation

    lateinit var font: Font

    lateinit var music: Music

    lateinit var jumpSound: Sound
    lateinit var highJumpSound: Sound
    lateinit var hitSound: Sound
    lateinit var coinSound: Sound
    lateinit var clickSound: Sound

    fun load(game: GLGame) {
        background = Texture(game, "background.png")
        backgroundRegion = TextureRegion(background, 0f, 0f, 320f, 480f)

        items = Texture(game, "items.png")
        mainMenu = TextureRegion(items, 0f, 224f, 300f, 110f)
        pauseMenu = TextureRegion(items, 224f, 128f, 192f, 96f)
        ready = TextureRegion(items, 320f, 224f, 192f, 32f)
        gameOver = TextureRegion(items, 352f, 256f, 160f, 96f)
        highScoreRegion = TextureRegion(items, 0f, 257f, 300f, 110f / 3)
        logo = TextureRegion(items, 0f, 352f, 274f, 142f)
        soundOff = TextureRegion(items, 0f, 0f, 64f, 64f)
        soundOn = TextureRegion(items, 64f, 0f, 64f, 64f)
        arrow = TextureRegion(items, 0f, 64f, 64f, 64f)
        pause = TextureRegion(items, 64f, 64f, 64f, 64f)

        spring = TextureRegion(items, 128f, 0f, 32f, 32f)
        castle = TextureRegion(items, 128f, 64f, 64f, 64f)

        coinAnim = Animation(
            0.2f,
            TextureRegion(items, 128f, 32f, 32f, 32f),
            TextureRegion(items, 160f, 32f, 32f, 32f),
            TextureRegion(items, 192f, 32f, 32f, 32f),
            TextureRegion(items, 160f, 32f, 32f, 32f)
        )
        bobJump = Animation(
            0.2f,
            TextureRegion(items, 0f, 128f, 32f, 32f),
            TextureRegion(items, 32f, 128f, 32f, 32f)
        )
        bobFall = Animation(
            0.2f,
            TextureRegion(items, 64f, 128f, 32f, 32f),
            TextureRegion(items, 96f, 128f, 32f, 32f)
        )
        bobHit = TextureRegion(items, 128f, 128f, 32f, 32f)
        squirellFly = Animation(
            0.2f,
            TextureRegion(items, 0f, 160f, 32f, 32f),
            TextureRegion(items, 32f, 160f, 32f, 32f)
        )
        platform = TextureRegion(items, 64f, 160f, 64f, 16f)
        brakingPlatform = Animation(
            0.2f,
            TextureRegion(items, 64f, 160f, 64f, 16f),
            TextureRegion(items, 64f, 176f, 64f, 16f),
            TextureRegion(items, 64f, 192f, 64f, 16f),
            TextureRegion(items, 64f, 208f, 64f, 16f)
        )

        font = Font(items, 224, 0, 16, 16, 20)

        music = game.getAudio().newMusic("music.mp3")
        music.setLooping(true)
        music.setVolume(0.5f)
        if(Settings.soundEnabled) music.play()
        jumpSound = game.getAudio().newSound("jump.ogg")
        highJumpSound = game.getAudio().newSound("highjump.ogg")
        hitSound = game.getAudio().newSound("hit.ogg")
        coinSound = game.getAudio().newSound("coin.ogg")
        clickSound = game.getAudio().newSound("click.ogg")
    }

    fun reload(){
        background.reload()
        items.reload()
        if(Settings.soundEnabled) music.play()
    }

    fun playSound(sound: Sound){
        if(Settings.soundEnabled) sound.play(1f)
    }


}