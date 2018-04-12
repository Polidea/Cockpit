package com.polidea.cockpit.utils

import android.content.Context
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter


class FileUtils private constructor(){
    var cockpitManager = CockpitManager.getInstance()
    private val loaderOptions = LoaderOptions()
    val yaml: Yaml = Yaml(loaderOptions)

    fun saveCockpitAsYaml() {
        loaderOptions.isAllowDuplicateKeys = false
        val fileWriter = FileWriter(SAVED_COCKPIT_FILE_PATH)

        val data: LinkedHashMap<String, Any> = LinkedHashMap()
        cockpitManager.params.forEach {
            data[it.name] = it.value
        }

        yaml.dump(data, fileWriter)
    }

    fun readCockpitFromFile() {
        if (!File(SAVED_COCKPIT_FILE_PATH).exists()) {
            return
        }

        val savedCockpit = File(SAVED_COCKPIT_FILE_PATH)
        val list: Map<String, Any> = yaml.load(savedCockpit.bufferedReader().use {
            it.readText()
        })

        list.forEach {
            cockpitManager.addParam(CockpitParam(it.key, it.value.javaClass, it.value))
        }
    }

    companion object {
        private var SAVED_COCKPIT_FILE_PATH: String? = null
        private var INSTANCE: FileUtils? = null

        fun init(context: Context) {
            SAVED_COCKPIT_FILE_PATH = context.filesDir.path + File.separator + "savedCockpit.yml"
        }

        @JvmStatic
        fun getInstance(): FileUtils {
            val instance = INSTANCE ?: FileUtils()
            INSTANCE = instance
            return instance
        }
    }
}