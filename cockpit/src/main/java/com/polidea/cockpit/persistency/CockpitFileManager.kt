package com.polidea.cockpit.persistency

import android.content.res.AssetManager
import com.polidea.cockpit.core.Param
import java.io.File

abstract class CockpitFileManager(private val filesDirPath: String, protected val assetManager: AssetManager) {

    protected abstract val savedCockpitFileName: String

    protected abstract val inputCockpitFileName: String

    protected val savedCockpitFilePath by lazy {
        filesDirPath + File.separator + savedCockpitFileName
    }

    abstract fun readInputParams(): List<Param<*>>

    abstract fun readSavedParams(): List<Param<*>>

    abstract fun saveParams(params: List<Param<*>>)
}