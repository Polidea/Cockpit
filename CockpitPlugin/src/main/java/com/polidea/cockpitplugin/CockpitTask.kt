package com.polidea.cockpitplugin

import com.polidea.cockpitplugin.generator.DebugCockpitGenerator
import com.polidea.cockpitplugin.generator.ReleaseCockpitGenerator
import com.polidea.cockpitplugin.input.FileFactory
import com.polidea.cockpitplugin.input.InputFilesProvider
import com.polidea.cockpitplugin.model.*
import com.polidea.cockpitplugin.util.Util
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter
import java.io.Serializable

class Flavor(val name: String, val dimension: String): Serializable

open class CockpitTask : DefaultTask() {
    private val cockpitDirectoryPath = "cockpit/"
    private val cockpitOutputDirectory = "${project.buildDir}/generated/source/cockpit/"
    private val cockpitAssetsOutputDirectory = "${project.buildDir}/generated/assets/"
    private val yaml = Yaml(yamlLoaderOptions())

    private val inputFilesProvider = InputFilesProvider(cockpitDirectoryPath, object: FileFactory {
        override fun file(path: String): File {
            return project.file(path)
        }

        override fun isFileExists(file: File): Boolean {
            return file.exists()
        }
    })

    @Input
    var variantName: String? = null

    @Input
    var buildTypeName: String? = null

    @Input
    var variantDirName: String? = null

    @Input
    var flavorDimensionList: List<String>? = null

    @Input
    var productFlavorList: List<Flavor>? = null

    @Input
    var buildTypeList: List<String>? = null

    @TaskAction
    fun CockpitAction() {
        val cockpitMaps = cockpitFiles().map { loadYaml(it) }.toMutableList()

        if (cockpitMaps.isNotEmpty()) {
            val onto = cockpitMaps.removeAt(0).toMutableMap()
            val mergedMap = Util.deepMerge(onto, *cockpitMaps.toTypedArray())

            val mergedCockpitFile = File(getCockpitAssetsOutputDirectory(), "mergedCockpit.yml")
            saveToYaml(mergedCockpitFile, mergedMap)

            val params: List<Param<*>> = parseValues(mergedMap)
            val generator = if (buildTypeName.isRelease()) ReleaseCockpitGenerator() else DebugCockpitGenerator()
            generator.generate(params, getCockpitOutputDirectory())
        } else {
            throw IllegalStateException("Empty cockpit map collection. Please make sure, you added your .yml files to $cockpitDirectoryPath directory, NOT to src/<variant>/assets")
        }
    }

    @InputFiles
    fun cockpitFiles(): List<File> {
        val dimensions = flavorDimensionList ?: return emptyList()
        val flavors = productFlavorList ?: return emptyList()
        val variantName = this.variantName ?: return emptyList()
        val buildTypes = buildTypeList ?: return emptyList()

        return inputFilesProvider.cockpitFiles(dimensions, flavors, variantName, buildTypes)
    }

    @OutputDirectory
    fun getCockpitOutputDirectory(): File {
        return project.file(cockpitOutputDirectory + "$variantDirName")
    }

    @OutputDirectory
    fun getCockpitAssetsOutputDirectory(): File {
        return project.file(cockpitAssetsOutputDirectory + "$variantDirName")
    }

    private fun parseValues(values: Map<String, Any>): ArrayList<Param<*>> {
        val paramList = ArrayList<Param<*>>()

        values.map {
            val value = it.value
            when (value) {
                is String -> paramList.add(StringParam(it.key, value))
                is Int -> paramList.add(IntegerParam(it.key, value))
                is Double -> paramList.add(DoubleParam(it.key, value))
                is Boolean -> paramList.add(BooleanParam(it.key, value))
                else -> throw IllegalArgumentException("Param type undefined: $it!")
            }
        }

        return paramList
    }

    private fun loadYaml(yamlFile: File): Map<String, Any> {
        return yaml.load(yamlFile.bufferedReader().use {
            it.readText()
        })
    }

    private fun saveToYaml(yamlFile: File, params: Map<String, Any>) {
        val writer = FileWriter(yamlFile)
        yaml.dump(params, writer)
    }

    private fun yamlLoaderOptions(): LoaderOptions {
        val loaderOptions = LoaderOptions()
        loaderOptions.isAllowDuplicateKeys = false
        return loaderOptions
    }

    private fun String?.isRelease() = "release" == this
}