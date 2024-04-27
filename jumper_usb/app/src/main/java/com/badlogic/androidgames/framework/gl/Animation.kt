package com.badlogic.androidgames.framework.gl

import java.lang.Math.min

class Animation(val frameDuration: Float, vararg val keyFrames: TextureRegion) {

    companion object {
        const val ANIMATION_LOOPING = 0
        const val ANIMATION_NONLOOPING = 1
    }

    fun getKeyFrame(stateTime: Float, mode: Int): TextureRegion {
        var frameNumber = (stateTime / frameDuration).toInt()

        if (mode == ANIMATION_NONLOOPING) {
            frameNumber = min(keyFrames.size - 1, frameNumber)
        } else {
            frameNumber = frameNumber % keyFrames.size
        }
        return keyFrames[frameNumber]

    }
}