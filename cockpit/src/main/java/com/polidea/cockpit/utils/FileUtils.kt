package com.polidea.cockpit.utils

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

    fun init(filesDirPath: String) {
        savedCockpitFilePath = filesDirPath + File.separator + "savedCockpit.yml"
    }

    fun saveCockpitAsYaml() {
        if (!::savedCockpitFilePath.isInitialized) {
            System.err.println("Cockpit is not initialized! Please make sure this is intentional.")
            return
        }

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
        val list: Map<String, Any> = yaml.load(savedCockpit.bufferedReader().use {
            it.readText()
        })

        list.forEach {
            CockpitManager.addParam(CockpitParam(it.key, it.value.javaClass, it.value))
        }
    }

}