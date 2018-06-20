package com.polidea.cockpitplugin

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter

class YamlReaderAndWriter {

    private val yaml = Yaml(yamlLoaderOptions())

    fun loadParamsFromYaml(yamlFile: File): Map<String, Any> {
        return yaml.load(yamlFile.bufferedReader().use {
            it.readText()
        })
    }

    fun saveParamsToYaml(params: Map<String, Any>, yamlFile: File) {
        val writer = FileWriter(yamlFile)
        yaml.dump(params, writer)
    }

    private fun yamlLoaderOptions(): LoaderOptions {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        return loaderOptions
    }
}