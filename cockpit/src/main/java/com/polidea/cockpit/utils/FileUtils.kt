package com.polidea.cockpit.utils

import android.content.res.AssetManager
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
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

        inputParams.forEach {
            val extendedParam = savedParams[it.key] ?: it.value
            val description = extendedParam.description
            val value = extendedParam.value
            val group = extendedParam.group
            CockpitManager.addParam(CockpitParam(it.key, value, description, group))
        }
    }

}