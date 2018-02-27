package com.polidea.androidtweaks.utils

import android.content.Context
import com.polidea.androidtweaks.manager.TweakParam
import com.polidea.androidtweaks.manager.TweaksManager
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter


class FileUtils(context: Context) {
    private val savedTweaksFilePath = context.filesDir.path + File.separator + "savedTweaks.yml"

    fun saveTweaksAsYaml() {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        val yaml = Yaml(loaderOptions)
        val fileWriter = FileWriter(savedTweaksFilePath)

        val data: LinkedHashMap<String, Any> = LinkedHashMap()
        TweaksManager.getInstance().params.forEach {
            data[it.name] = it.value
        }

        yaml.dump(data, fileWriter)
    }

    fun readTweaksFromFile() {
        if (!File(savedTweaksFilePath).exists()) {
            return
        }

        val savedTweaks = File(savedTweaksFilePath)
        System.out.println(savedTweaks.readText())

        val yaml = Yaml()
        val list: Map<String, Any> = yaml.load(savedTweaks.bufferedReader().use {
            it.readText()
        })

        list.forEach {
            TweaksManager.getInstance().addParam(TweakParam(it.key, it.value.javaClass.kotlin, it.value))
        }
    }
}