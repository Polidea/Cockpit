package com.polidea.cockpit.utils

import android.content.res.AssetManager
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter


object FileUtils {
    private lateinit var savedCockpitFilePath: String
    private val loaderOptions = LoaderOptions()
    private val yaml: Yaml = Yaml(loaderOptions)
    internal lateinit var inputParamsProvider: InputParamsProvider

    fun init(filesDirPath: String, assetManager: AssetManager) {
        savedCockpitFilePath = filesDirPath + File.separator + "savedCockpit.yml"
        inputParamsProvider = InputParamsProvider(assetManager)
    }

    fun saveCockpitAsYaml() {
        loaderOptions.isAllowDuplicateKeys = false
        val fileWriter = FileWriter(savedCockpitFilePath)

        val data: LinkedHashMap<String, Any> = LinkedHashMap()
        CockpitManager.params.forEach {
            data[it.name] = it.value
        }

        yaml.dump(data, fileWriter)
    }

    fun readCockpitFromFile() {
        if (!File(savedCockpitFilePath).exists()) {
            return
        }

        val savedCockpit = File(savedCockpitFilePath)
        val savedParamsMap: Map<String, Any> = yaml.load(savedCockpit.bufferedReader().use {
            it.readText()
        })

        val inputMap: Map<String, Any> = inputParamsProvider.getInputParams()

        savedParamsMap.forEach {
            if (inputMap.contains(it.key)) {
                CockpitManager.addParam(CockpitParam(it.key, it.value.javaClass, it.value))
            }
        }
    }
}

class InputParamsProvider(val assetManager: AssetManager) {
    fun getInputParams(): Map<String, Any> {
        return Yaml(LoaderOptions()).load(assetManager.open("cockpit.yml").bufferedReader().use {
            it.readText()
        })
    }
}