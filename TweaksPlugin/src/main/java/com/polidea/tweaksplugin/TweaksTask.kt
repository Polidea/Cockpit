package com.polidea.tweaksplugin

import com.polidea.tweaksplugin.model.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml
import java.io.File

open class TweaksTask: DefaultTask() {
    @TaskAction
    fun TweaksAction() {
        System.out.println("HELLO TWEAKS") // TODO remove, test
        val params: List<Param<*>> = parseYaml(settingsFile)
        TweaksGenerator().generate(params, getOutputFile())
    }

    @InputFile
    val settingsFile: File = project.file("src/main/assets/tweaks.yml")

    @OutputFile
    fun getOutputFile(): File {
        return project.file("${project.buildDir}/generated/source/tweaks")
    }

    fun parseYaml(yamlString: String): List<Param<*>> {
        val yaml = Yaml()
        val values: Map<String, Any> = yaml.load(yamlString.reader().use {
            it.readText()
        })
        val paramList = ArrayList<Param<*>>()

        for (v in values) {
            when (v.value) {
                is String -> paramList.add(StringParam(v.key, v.value as String))
                is Int -> paramList.add(IntegerParam(v.key, v.value as Int))
                is Double -> paramList.add(DoubleParam(v.key, v.value as Double))
                is Boolean -> paramList.add(BooleanParam(v.key, v.value as Boolean))
            }
        }

        return paramList
    }

    fun parseYaml(yamlFile: File): List<Param<*>> {
        val yaml = Yaml()
        val values: Map<String, Any> = yaml.load(yamlFile.bufferedReader().use {
            it.readText()
        })
        val paramList = ArrayList<Param<*>>()

        for (v in values) {
            when (v.value) {
                is String -> paramList.add(StringParam(v.key, v.value as String))
                is Int -> paramList.add(IntegerParam(v.key, v.value as Int))
                is Double -> paramList.add(DoubleParam(v.key, v.value as Double))
                is Boolean -> paramList.add(BooleanParam(v.key, v.value as Boolean))
            }
        }

        return paramList
    }
}