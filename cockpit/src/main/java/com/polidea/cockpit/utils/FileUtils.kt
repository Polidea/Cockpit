package com.polidea.cockpit.utils

import android.content.res.AssetManager
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.persistency.CockpitYamlFileManager


object FileUtils {

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
            val param = savedParams.firstOrNull { inputParam.name == it.name } ?: inputParam
            params.add(param)
        }
        return params
    }

    fun getDefaultParams() =
            cockpitYamlFileManager.readInputParams()

    fun getSavedParams() =
            cockpitYamlFileManager.readSavedParams()
}