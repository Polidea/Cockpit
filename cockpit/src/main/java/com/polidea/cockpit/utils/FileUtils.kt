package com.polidea.cockpit.utils

import android.content.res.AssetManager
import com.polidea.cockpit.core.YamlParam
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
            CockpitManager.addParam(mapToCockpitParam(it.key, extendedParam))
        }
        CockpitManager.defaultParams = inputParams.map { mapToCockpitParam(it.key, it.value) }.toMutableList()
    }

    private fun <T : Any> mapToCockpitParam(paramName: String, yaml: YamlParam<T>): CockpitParam<Any> {
        val description = yaml.description
        val value = yaml.value
        val group = yaml.group
        return CockpitParam(paramName, value, description, group)
    }

}