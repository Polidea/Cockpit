package com.polidea.cockpit.core

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter
import java.io.Reader

// TODO: extract to core library
class YamlReaderAndWriter {

    private val yaml = Yaml(yamlLoaderOptions())

    private val mapper = ParamsMapper()

    fun loadParamsFromYaml(yamlFile: File): List<Param<*>> {
        return loadParamsFromReader(yamlFile.bufferedReader())
    }

    fun loadParamsFromReader(reader: Reader): List<Param<*>> {
        return mapper.toListOfParams(yaml.load(reader.use {
            it.readText()
        }))
    }

    fun saveParamsToYaml(params: List<Param<*>>, yamlFile: File) {
        val writer = FileWriter(yamlFile)
        yaml.dump(mapper.toYamlMap(params), writer)
    }

    private fun yamlLoaderOptions(): LoaderOptions {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        return loaderOptions
    }
}