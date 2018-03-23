package com.polidea.cockpit.utils

import android.content.Context
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter


class FileUtils(context: Context) {
    var savedCockpitFilePath = context.filesDir.path + File.separator + "savedCockpit.yml"
    var cockpitManager = CockpitManager.getInstance()
    private val loaderOptions = LoaderOptions()
    val yaml: Yaml = Yaml(loaderOptions)

    fun saveCockpitAsYaml() {
        loaderOptions.isAllowDuplicateKeys = false
        val fileWriter = FileWriter(savedCockpitFilePath)

        val data: LinkedHashMap<String, Any> = LinkedHashMap()
        cockpitManager.params.forEach {
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
            cockpitManager.addParam(CockpitParam(it.key, it.value.javaClass, it.value))
        }
    }
}