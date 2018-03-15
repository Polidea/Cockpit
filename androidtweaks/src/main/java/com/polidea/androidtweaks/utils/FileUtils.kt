package com.polidea.androidtweaks.utils

import android.content.Context
import com.polidea.androidtweaks.manager.TweakParam
import com.polidea.androidtweaks.manager.TweaksManager
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter


class FileUtils(context: Context) {
    var savedTweaksFilePath = context.filesDir.path + File.separator + "savedTweaks.yml"
    var tweaksManager = TweaksManager.getInstance()
    private val loaderOptions = LoaderOptions()
    val yaml: Yaml = Yaml(loaderOptions)

    fun saveTweaksAsYaml() {
        loaderOptions.isAllowDuplicateKeys = false
        val fileWriter = FileWriter(savedTweaksFilePath)

        val data: LinkedHashMap<String, Any> = LinkedHashMap()
        tweaksManager.params.forEach {
            data[it.name] = it.value
        }

        yaml.dump(data, fileWriter)
    }

    fun readTweaksFromFile() {
        if (!File(savedTweaksFilePath).exists()) {
            return
        }

        val savedTweaks = File(savedTweaksFilePath)
        val list: Map<String, Any> = yaml.load(savedTweaks.bufferedReader().use {
            it.readText()
        })

        list.forEach {
            tweaksManager.addParam(TweakParam(it.key, it.value.javaClass, it.value))
        }
    }
}