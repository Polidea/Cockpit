package com.polidea.cockpitplugin.input

import com.polidea.cockpitplugin.Flavor
import java.io.File

class InputFilesProvider(private val cockpitDirectoryPath: String,
                         private val fileFactory: FileFactory) {

    fun cockpitFiles(dimensions: List<String>, flavors: List<Flavor>, variantName: String, buildTypes: List<String>): List<File> {
        // Let's assume we consider variant: "stagingDemoDebug"
        // We have to check following variants: staging, demo, debug, stagingDemo, stagingDemoDebug, main // unordered

        // stagingDemoDebug, debug, stagingDemo, staging, demo, main // from the most priority
        // main, demo, staging, stagingDemo, debug, stagingDemoDebug // from the least priority

        val ordered = ArrayList<String>()
        ordered.add("") // main
        val singleFlavors = dimensions.reversed().map { dimension ->
            flavors.find { it.dimension == dimension && variantName.contains(it.name, ignoreCase = true) }?.name
        }.filter { it != null }
                .map { it!!.capitalize() } // Demo, Staging

        ordered.addAll(singleFlavors)

        if (dimensions.size > 1) {
            val compositeFlavor = singleFlavors.reversed().fold(StringBuilder()) { acc: StringBuilder, flavor: String ->
                acc.append(flavor)
            }.toString() //StagingDemo

            ordered.add(compositeFlavor)
        }

        if (dimensions.isNotEmpty()) {
            buildTypes.find { variantName.contains(it, ignoreCase = true) }?.let {
                ordered.add(it.capitalize())
            } // debug
        }

        ordered.add(variantName.capitalize()) // stagingDemoDebug

        return ordered.map { fileFactory.file(cockpitDirectoryPath + "cockpit" + it + ".yml") }
                .filter { fileFactory.isFileExists(it) }
    }
}