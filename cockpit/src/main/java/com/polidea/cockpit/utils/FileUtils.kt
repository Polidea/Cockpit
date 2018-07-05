package com.polidea.cockpit.utils

import android.content.res.AssetManager
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.persistency.CockpitYamlFileManager
import org.jetbrains.annotations.TestOnly


internal object FileUtils {

    private lateinit var cockpitYamlFileManager: CockpitYamlFileManager

    fun init(filesDirPath: String, assetManager: AssetManager) {
        cockpitYamlFileManager = CockpitYamlFileManager(filesDirPath, assetManager)
    }

    fun saveCockpitAsYaml(params: List<CockpitParam<Any>>) {
        cockpitYamlFileManager.saveParams(params)
    }

    fun getParams(): List<CockpitParam<Any>> {
        val inputParams = getDefaultParams()
        val savedParams = getSavedParams()

        val params = mutableListOf<CockpitParam<Any>>()
        inputParams.forEach { inputParam ->
            // latest version of param: saved param, if exists, or default - otherwise
            val param = savedParams.firstOrNull { inputParam.name == it.name } ?: inputParam
            val lastValue = param.value
            // we need only value from saved param, the rest:
            // group, description, etc. should be taken from default (cockpit*.yml) param
            params.add(inputParam.copy(value = lastValue))
        }
        return params
    }

    fun getDefaultParams() =
            cockpitYamlFileManager.readInputParams()

    fun getSavedParams() =
            cockpitYamlFileManager.readSavedParams()

    @TestOnly
    fun setCockpitYamlFileManager(manager: CockpitYamlFileManager) {
        cockpitYamlFileManager = manager
    }
}