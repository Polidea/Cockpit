package com.polidea.tweaksplugin

import com.polidea.tweaksplugin.generator.DebugTweaksGenerator
import com.polidea.tweaksplugin.generator.ReleaseTweaksGenerator
import com.polidea.tweaksplugin.model.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File

open class TweaksTask : DefaultTask() {
    private val tweaksFilePath = "src/main/assets/tweaks.yml"
    private val tweaksOutputDirectory = "${project.buildDir}/generated/source/tweaks/"

    @Input
    var variantName: String? = null

    @Input
    var buildTypeName: String? = null

    @TaskAction
    fun TweaksAction() {
        val params: List<Param<*>> = parseYaml(tweaksFile)
        val generator = if (buildTypeName.isRelease()) ReleaseTweaksGenerator() else DebugTweaksGenerator()
        generator.generate(params, getTweaksOutputDirectory())
    }

    @InputFile
    val tweaksFile: File = project.file(tweaksFilePath)

    @OutputDirectory
    fun getTweaksOutputDirectory(): File {
        return project.file(tweaksOutputDirectory + "$variantName")
    }

    fun parseYaml(yamlFile: File): List<Param<*>> {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        val yaml = Yaml(loaderOptions)
        val values: Map<String, Any> = yaml.load(yamlFile.bufferedReader().use {
            it.readText()
        })
        val paramList = ArrayList<Param<*>>()

        values.map {
            when (it.value) {
                is String -> paramList.add(StringParam(it.key, it.value as String))
                is Int -> paramList.add(IntegerParam(it.key, it.value as Int))
                is Double -> paramList.add(DoubleParam(it.key, it.value as Double))
                is Boolean -> paramList.add(BooleanParam(it.key, it.value as Boolean))
                else -> throw IllegalArgumentException("Param type undefined: $it!")
            }
        }

        return paramList
    }

    private fun String?.isRelease() = "release" == this
}