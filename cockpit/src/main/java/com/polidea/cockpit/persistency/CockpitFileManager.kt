package com.polidea.cockpit.persistency

import android.content.res.AssetManager
import com.polidea.cockpit.manager.CockpitParam
import java.io.File

abstract class CockpitFileManager(private val filesDirPath: String, protected val assetManager: AssetManager) {

    protected abstract val savedCockpitFileName: String

    protected abstract val inputCockpitFileName: String

    protected val savedCockpitFilePath by lazy {
        filesDirPath + File.separator + savedCockpitFileName
    }

    abstract fun readInputParams(): Map<String, Any>

    abstract fun readSavedParams(): Map<String, Any>

    abstract fun saveParams(params: List<CockpitParam<Any>>)
}