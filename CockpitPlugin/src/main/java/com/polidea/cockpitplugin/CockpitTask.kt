package com.polidea.cockpitplugin

import com.polidea.cockpitplugin.generator.DebugCockpitGenerator
import com.polidea.cockpitplugin.generator.ReleaseCockpitGenerator
import com.polidea.cockpitplugin.model.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File

open class CockpitTask : DefaultTask() {
    private val cockpitFilePath = "src/main/assets/cockpit.yml"
    private val cockpitOutputDirectory = "${project.buildDir}/generated/source/cockpit/"

    @Input
    var variantName: String? = null

    @Input
    var buildTypeName: String? = null

    @TaskAction
    fun CockpitAction() {
        val params: List<Param<*>> = parseYaml(cockpitFile)
        val generator = if (buildTypeName.isRelease()) ReleaseCockpitGenerator() else DebugCockpitGenerator()
        generator.generate(params, getCockpitOutputDirectory())
    }

    @InputFile
    val cockpitFile: File = project.file(cockpitFilePath)

    @OutputDirectory
    fun getCockpitOutputDirectory(): File {
        return project.file(cockpitOutputDirectory + "$variantName")
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