package com.polidea.cockpit.persistency

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.YamlReaderAndWriter
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.File

class CockpitYamlFileManager(filesDirPath: String, assetManager: AssetManager) : CockpitFileManager(filesDirPath, assetManager) {

    override val savedCockpitFileName = "savedCockpit.yml"
    override val inputCockpitFileName = "mergedCockpit.yml"
    private val yamlReaderAndWriter = YamlReaderAndWriter()

    override fun readInputParams(): List<CockpitParam<Any>> =
            readSavedParams(assetManager.open(inputCockpitFileName).bufferedReader())


    override fun readSavedParams(): List<CockpitParam<Any>> {

        val savedParamsFile = File(savedCockpitFilePath)

        if (!savedParamsFile.exists()) {
            return emptyList()
        }

        return readSavedParams(savedParamsFile.bufferedReader())
    }

    override fun saveParams(params: List<CockpitParam<Any>>) {
        val file = File(savedCockpitFilePath)
        yamlReaderAndWriter.saveParamsToYaml(params, file)
    }

    private fun readSavedParams(bufferedReader: BufferedReader): List<CockpitParam<Any>> {
        return yamlReaderAndWriter.loadParamsFromReader(bufferedReader)
    }
}