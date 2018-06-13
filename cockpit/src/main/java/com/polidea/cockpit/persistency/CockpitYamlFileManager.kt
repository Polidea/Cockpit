package com.polidea.cockpit.persistency

import android.content.res.AssetManager
import com.polidea.cockpit.manager.CockpitParam
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.util.*

class CockpitYamlFileManager(filesDirPath: String, assetManager: AssetManager) : CockpitFileManager(filesDirPath, assetManager) {

    override val savedCockpitFileName = "savedCockpit.yml"
    override val inputCockpitFileName = "mergedCockpit.yml"
    private val yaml: Yaml

    init {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        yaml = Yaml(loaderOptions)
    }

    override fun readInputParams(): Map<String, Any> =
            readSavedParams(assetManager.open(inputCockpitFileName).bufferedReader())


    override fun readSavedParams(): Map<String, Any> {

        val savedParamsFile = File(savedCockpitFilePath)

        if (!savedParamsFile.exists()) {
            return emptyMap()
        }

        return readSavedParams(savedParamsFile.bufferedReader())
    }

    override fun saveParams(params: List<CockpitParam>) {
        val fileWriter = FileWriter(savedCockpitFilePath)

        val data: LinkedHashMap<String, Any> = LinkedHashMap()
        params.forEach {
            data[it.name] = it.value
        }

        yaml.dump(data, fileWriter)
    }

    private fun readSavedParams(bufferedReader: BufferedReader): Map<String, Any> {
        return yaml.load(bufferedReader.use {
            it.readText()
        })
    }
}