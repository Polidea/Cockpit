package com.polidea.cockpit.persistency

import android.content.res.AssetManager
import com.polidea.cockpit.core.Param
import com.polidea.cockpit.core.YamlReaderAndWriter
import java.io.BufferedReader
import java.io.File

class CockpitYamlFileManager(filesDirPath: String, assetManager: AssetManager) : CockpitFileManager(filesDirPath, assetManager) {

    override val savedCockpitFileName = "savedCockpit.yml"
    override val inputCockpitFileName = "mergedCockpit.yml"
    private val yamlReaderAndWriter = YamlReaderAndWriter()

    override fun readInputParams(): List<Param<*>> =
            readSavedParams(assetManager.open(inputCockpitFileName).bufferedReader())


    override fun readSavedParams(): List<Param<*>> {

        val savedParamsFile = File(savedCockpitFilePath)

        if (!savedParamsFile.exists()) {
            return emptyList()
        }

        return readSavedParams(savedParamsFile.bufferedReader())
    }

    override fun saveParams(params: List<Param<*>>) {
        val file = File(savedCockpitFilePath)
        yamlReaderAndWriter.saveParamsToYaml(params, file)
    }

    private fun readSavedParams(bufferedReader: BufferedReader): List<Param<*>> {
        return yamlReaderAndWriter.loadParamsFromReader(bufferedReader)
    }
}