package com.polidea.cockpitplugin

import com.polidea.cockpitplugin.core.YamlReaderAndWriter
import com.polidea.cockpitplugin.generator.DebugCockpitGenerator
import com.polidea.cockpitplugin.generator.ReleaseCockpitGenerator
import com.polidea.cockpitplugin.input.FileFactory
import com.polidea.cockpitplugin.input.InputFilesProvider
import com.polidea.cockpitplugin.model.*
import com.polidea.cockpitplugin.util.Util
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File
import java.io.Serializable

class Flavor(val name: String, val dimension: String) : Serializable

open class CockpitTask : DefaultTask() {
    private val cockpitDirectoryPath = "cockpit/"
    private val cockpitOutputDirectory = "${project.buildDir}/generated/source/cockpit/"
    private val cockpitAssetsOutputDirectory = "${project.buildDir}/generated/assets/"
    private val yamlReaderAndWriter = YamlReaderAndWriter()

    private val inputFilesProvider = InputFilesProvider(cockpitDirectoryPath, object : FileFactory {
        override fun file(path: String): File {
            return project.file(path)
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
        val cockpitLists = cockpitFiles().map { yamlReaderAndWriter.loadParamsFromYaml(it) }

        if (cockpitLists.isNotEmpty()) {
            val cockpitMaps = cockpitLists.map { linkedMapOf(*it.map { Pair(it.name, it) }.toTypedArray()) }.toMutableList()

            val lowestProrityMap = cockpitMaps.removeAt(0).toMutableMap()
            val mergedParametersMap = Util.deepMerge(lowestProrityMap, *cockpitMaps.toTypedArray())

            val mergedParametersList = mergedParametersMap.values.toList()
            val mergedCockpitFile = File(getCockpitAssetsOutputDirectory(), "mergedCockpit.yml")

            yamlReaderAndWriter.saveParamsToYaml(mergedParametersList, mergedCockpitFile)

            val generator = if (buildTypeName.isRelease()) ReleaseCockpitGenerator() else DebugCockpitGenerator()
            generator.generate(mergedParametersList, getCockpitOutputDirectory())
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

        return inputFilesProvider.getAllCockpitFilesForCurrentVariant(dimensions, flavors, variantName, buildTypes)
                .filter { it.exists() }

    }

    @OutputDirectory
    fun getCockpitOutputDirectory(): File {
        return project.file(cockpitOutputDirectory + "$variantDirName")
    }

    @OutputDirectory
    fun getCockpitAssetsOutputDirectory(): File {
        return project.file(cockpitAssetsOutputDirectory + "$variantDirName")
    }

    private fun String?.isRelease() = "release" == this
}