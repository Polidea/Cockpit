package com.polidea.cockpit.core

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter
import java.io.Reader

class YamlReaderAndWriter {

    private val yaml = Yaml(yamlLoaderOptions())

    private val mapper = ParamsMapper()

    fun loadParamsFromYaml(yamlFile: File): List<CockpitParam<Any>> {
        return loadParamsFromReader(yamlFile.bufferedReader())
    }

    fun loadParamsFromReader(reader: Reader): List<CockpitParam<Any>> {
        return mapper.toListOfParams(yaml.load(reader.use {
            it.readText()
        }))
    }

    fun saveParamsToYaml(params: List<CockpitParam<Any>>, yamlFile: File) {
        val writer = FileWriter(yamlFile)
        yaml.dump(mapper.toYamlMap(params), writer)
    }

    private fun yamlLoaderOptions(): LoaderOptions {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        return loaderOptions
    }
}