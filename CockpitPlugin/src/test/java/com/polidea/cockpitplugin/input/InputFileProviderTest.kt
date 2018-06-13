package com.polidea.cockpitplugin.input

import com.polidea.cockpitplugin.Flavor
import org.junit.Ignore
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals


class InputFileProviderTest {

    /* ******** No flavor dimensions test ******** */

    @Test
    fun createCockpitFilesDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "debug"
        val fileNames = inputFilesProvider.cockpitFiles(noDimensions, noFlavors, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "release"
        val fileNames = inputFilesProvider.cockpitFiles(noDimensions, noFlavors, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    /* ******** One flavor dimension test ******** */

    @Test
    fun createCockpitFilesProdDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "prodDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApi, flavorsApi, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitProd.yml",
                "cockpitDebug.yml",
                "cockpitProdDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesProdReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "prodRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApi, flavorsApi, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitProd.yml",
                "cockpitRelease.yml",
                "cockpitProdRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "stagingDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApi, flavorsApi, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitStaging.yml",
                "cockpitDebug.yml",
                "cockpitStagingDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "stagingRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApi, flavorsApi, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitStaging.yml",
                "cockpitRelease.yml",
                "cockpitStagingRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    /* ******** Two flavor dimensions test ******** */

    @Test
    fun createCockpitFilesProdDemoDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "prodDemoDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDemo.yml",
                "cockpitProd.yml",
                "cockpitProdDemo.yml",
                "cockpitDebug.yml",
                "cockpitProdDemoDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesProdDemoReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "prodDemoRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDemo.yml",
                "cockpitProd.yml",
                "cockpitProdDemo.yml",
                "cockpitRelease.yml",
                "cockpitProdDemoRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesProdFullDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "prodFullDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitFull.yml",
                "cockpitProd.yml",
                "cockpitProdFull.yml",
                "cockpitDebug.yml",
                "cockpitProdFullDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesProdFullReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "prodFullRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitFull.yml",
                "cockpitProd.yml",
                "cockpitProdFull.yml",
                "cockpitRelease.yml",
                "cockpitProdFullRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingDemoDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "stagingDemoDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDemo.yml",
                "cockpitStaging.yml",
                "cockpitStagingDemo.yml",
                "cockpitDebug.yml",
                "cockpitStagingDemoDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingDemoReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "stagingDemoRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDemo.yml",
                "cockpitStaging.yml",
                "cockpitStagingDemo.yml",
                "cockpitRelease.yml",
                "cockpitStagingDemoRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingFullDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "stagingFullDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitFull.yml",
                "cockpitStaging.yml",
                "cockpitStagingFull.yml",
                "cockpitDebug.yml",
                "cockpitStagingFullDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingFullReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "stagingFullRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitFull.yml",
                "cockpitStaging.yml",
                "cockpitStagingFull.yml",
                "cockpitRelease.yml",
                "cockpitStagingFullRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingFullDebugOnlyMainAndDebugTest() {
        val inputFilesProvider = createInputFilesProvider {
            when (it.name) {
                "cockpit.yml" -> true
                "cockpitDebug.yml" -> true
                else -> false
            }
        }
        val variantName = "stagingFullDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesStagingFullDebugOnlyMainTest() {
        val inputFilesProvider = createInputFilesProvider {
            when (it.name) {
                "cockpit.yml" -> true
                else -> false
            }
        }
        val variantName = "stagingFullDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }


    @Test
    fun createCockpitFilesProdFullReleaseOnlyMainAndDebugTest() {
        val inputFilesProvider = createInputFilesProvider {
            when (it.name) {
                "cockpit.yml" -> true
                "cockpitDebug.yml" -> true
                else -> false
            }
        }
        val variantName = "prodFullRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiMode, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    /* ******** Reversed two flavor dimensions test ******** */

    @Test
    fun createCockpitFilesDemoProdDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "demoProdDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitProd.yml",
                "cockpitDemo.yml",
                "cockpitDemoProd.yml",
                "cockpitDebug.yml",
                "cockpitDemoProdDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesDemoProdReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "demoProdRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitProd.yml",
                "cockpitDemo.yml",
                "cockpitDemoProd.yml",
                "cockpitRelease.yml",
                "cockpitDemoProdRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullProdDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "fullProdDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitProd.yml",
                "cockpitFull.yml",
                "cockpitFullProd.yml",
                "cockpitDebug.yml",
                "cockpitFullProdDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullProdReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "fullProdRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitProd.yml",
                "cockpitFull.yml",
                "cockpitFullProd.yml",
                "cockpitRelease.yml",
                "cockpitFullProdRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesDemoStagingDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "demoStagingDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitStaging.yml",
                "cockpitDemo.yml",
                "cockpitDemoStaging.yml",
                "cockpitDebug.yml",
                "cockpitDemoStagingDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesDemoStagingReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "demoStagingRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitStaging.yml",
                "cockpitDemo.yml",
                "cockpitDemoStaging.yml",
                "cockpitRelease.yml",
                "cockpitDemoStagingRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullStagingDebugTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "fullStagingDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitStaging.yml",
                "cockpitFull.yml",
                "cockpitFullStaging.yml",
                "cockpitDebug.yml",
                "cockpitFullStagingDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullStagingReleaseTest() {
        val inputFilesProvider = createInputFilesProvider { true }
        val variantName = "fullStagingRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitStaging.yml",
                "cockpitFull.yml",
                "cockpitFullStaging.yml",
                "cockpitRelease.yml",
                "cockpitFullStagingRelease.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullStagingDebugOnlyMainAndDebugTest() {
        val inputFilesProvider = createInputFilesProvider {
            when (it.name) {
                "cockpit.yml" -> true
                "cockpitDebug.yml" -> true
                else -> false
            }
        }
        val variantName = "fullStagingDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml",
                "cockpitDebug.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullStagingDebugOnlyMainTest() {
        val inputFilesProvider = createInputFilesProvider {
            when (it.name) {
                "cockpit.yml" -> true
                else -> false
            }
        }
        val variantName = "fullStagingDebug"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    @Test
    fun createCockpitFilesFullStagingReleaseOnlyMainAndDebugTest() {
        val inputFilesProvider = createInputFilesProvider {
            when (it.name) {
                "cockpit.yml" -> true
                "cockpitDebug.yml" -> true
                else -> false
            }
        }
        val variantName = "fullStagingRelease"
        val fileNames = inputFilesProvider.cockpitFiles(dimensionsApiModeReversed, flavorsApiMode, variantName, buildTypes)
                .map { it.name }
        val expectedFileNames = listOf(
                "cockpit.yml"
        )
        assertEquals(expectedFileNames, fileNames)
    }

    /* ******** private methods ******** */

    private fun createFileFactory(fileExistsClosure: (File) -> Boolean): FileFactory {
        return object : FileFactory {
            override fun file(path: String): File {
                return File(path)
            }

            override fun isFileExists(file: File): Boolean {
                return fileExistsClosure(file)
            }
        }
    }

    private fun createInputFilesProvider(fileExistsClosure: (File) -> Boolean): InputFilesProvider {
        val fileFactory = createFileFactory(fileExistsClosure)
        return InputFilesProvider("directory/", fileFactory)
    }

    companion object {

        val noDimensions = listOf<String>()
        val dimensionsApi = listOf("api")
        val dimensionsApiMode = listOf("api", "mode")
        val dimensionsApiModeReversed = dimensionsApiMode.reversed()

        val noFlavors = listOf<Flavor>()
        val flavorsApi = listOf(
                Flavor("prod", "api"),
                Flavor("staging", "api")
        )
        val flavorsApiMode = listOf(
                Flavor("demo", "mode"),
                Flavor("full", "mode"),
                Flavor("prod", "api"),
                Flavor("staging", "api")
        )

        val buildTypes = listOf("debug", "release")
    }
}