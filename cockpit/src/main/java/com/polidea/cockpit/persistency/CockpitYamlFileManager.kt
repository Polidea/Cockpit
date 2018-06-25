package com.polidea.cockpit.persistency

import android.content.res.AssetManager
import com.polidea.cockpit.core.YamlParam
import com.polidea.cockpit.manager.CockpitParam
import com.polidea.cockpit.core.YamlReaderAndWriter
import java.io.BufferedReader
import java.io.File

class CockpitYamlFileManager(filesDirPath: String, assetManager: AssetManager) : CockpitFileManager(filesDirPath, assetManager) {

    override val savedCockpitFileName = "savedCockpit.yml"
    override val inputCockpitFileName = "mergedCockpit.yml"
    private val yamlReaderAndWriter = YamlReaderAndWriter()

    override fun readInputParams(): Map<String, YamlParam<*>> =
            readSavedParams(assetManager.open(inputCockpitFileName).bufferedReader())


    override fun readSavedParams(): Map<String, YamlParam<*>> {

        val savedParamsFile = File(savedCockpitFilePath)

        if (!savedParamsFile.exists()) {
            return emptyMap()
        }

        return readSavedParams(savedParamsFile.bufferedReader())
    }

    override fun saveParams(params: List<CockpitParam<Any>>) {
        val file = File(savedCockpitFilePath)

        val data = linkedMapOf(*params.map { Pair(it.name, YamlParam(it.description, it.value, it.group)) }.toTypedArray())
        yamlReaderAndWriter.saveParamsToYaml(data, file)
    }

    private fun readSavedParams(bufferedReader: BufferedReader): Map<String, YamlParam<*>> {
        return yamlReaderAndWriter.loadParamsFromReader(bufferedReader)
    }
}