package com.polidea.cockpit.utils

import android.content.res.AssetManager
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.persistency.CockpitYamlFileManager


object FileUtils {

    lateinit var cockpitYamlFileManager: CockpitYamlFileManager

    fun init(filesDirPath: String, assetManager: AssetManager) {
        cockpitYamlFileManager = CockpitYamlFileManager(filesDirPath, assetManager)
    }

    fun saveCockpitAsYaml() {
        cockpitYamlFileManager.saveParams(CockpitManager.params)
    }

    fun loadCockpitParams() {
        val inputParams = cockpitYamlFileManager.readInputParams()
        val savedParams = cockpitYamlFileManager.readSavedParams()

        inputParams.forEach { inputParam ->
            val latestParam = savedParams.find { it.name == inputParam.name } ?: inputParam
            CockpitManager.addParam(latestParam)
        }
        CockpitManager.defaultParams = inputParams.toMutableList()
    }

}