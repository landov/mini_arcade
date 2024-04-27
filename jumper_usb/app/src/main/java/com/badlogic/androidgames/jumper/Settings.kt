package com.badlogic.androidgames.jumper

import com.badlogic.androidgames.framework.FileIO
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object Settings {
    var soundEnabled = true
    var highscores = intArrayOf(100,80,50,30,10)
    var file = ".superjumper"

    fun load(files: FileIO){
        var in_: BufferedReader? = null
        try {
            in_ = BufferedReader(InputStreamReader(files.readFile(file)))
            soundEnabled = in_.readLine().toBoolean()
            for (i in 0..4) {
                highscores[i] = in_.readLine().toInt()
            }
        } catch (e: Exception) {

            e.printStackTrace()
        } finally {
            try {
                if (in_ != null) in_.close()
            } catch (_: IOException) {
            }
        }
    }

    fun save(files: FileIO){
        var out: BufferedWriter? = null
        try {
            out = BufferedWriter(OutputStreamWriter(files.writeFile(file)))
            out.write(soundEnabled.toString())
            out.newLine()
            for (i in 0..4) {
                out.write(highscores[i].toString())
                out.newLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (_: IOException) {
            }
        }
    }

    fun addScore(score: Int) {
        for (i in 0..4) {
            if (highscores.get(i) < score) {
                for (j in 4 downTo i + 1) highscores.set(j,highscores.get(j - 1))
                highscores.set(i,score)
                break
            }
        }
    }
}