package com.badlogic.androidgames.framework.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.badlogic.androidgames.framework.FileIO
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class AndroidFileIO(private val context: Context) : FileIO {

    private val  assets = context.assets
    private var externalStoragePath = context.getExternalFilesDir(null)?.absolutePath+ File.separator

    override fun readAsset(fileName: String): InputStream {
        return assets.open(fileName)
    }

    override fun readFile(fileName: String): InputStream {
        return FileInputStream(externalStoragePath+fileName)
    }

    override fun writeFile(fileName: String): OutputStream {
        return FileOutputStream(externalStoragePath+fileName)
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}