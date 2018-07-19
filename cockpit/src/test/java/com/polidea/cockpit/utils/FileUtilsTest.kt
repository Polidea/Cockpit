package com.polidea.cockpit.utils

import android.content.Context
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.persistency.CockpitYamlFileManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilsTest {

    private val context: Context = mockk(relaxed = true)
    private val cockpitYamlFileManager: CockpitYamlFileManager = spyk(CockpitYamlFileManager(DIRECTORY_PATH, context.assets))

    init {
        File(DIRECTORY_PATH).mkdirs()
        FileUtils.init(DIRECTORY_PATH, context.assets)
    }

    @BeforeTest
    fun setup() {
        every { context.assets } returns mockk(relaxed = true)
        FileUtils.setCockpitYamlFileManager(cockpitYamlFileManager)

        every { cockpitYamlFileManager.readInputParams() } returns getTestCockpitParams()
        every { cockpitYamlFileManager.readSavedParams() } returns emptyList()
    }

    @Test
    fun saveAndReadCockpit() {
        getTestCockpitParams().forEach { CockpitManager.addParam(it) }
        FileUtils.saveCockpitAsYaml(CockpitManager.getParamsCopy())
        CockpitManager.clear()
        FileUtils.getParams().forEach { CockpitManager.addParam(it) }
        assertEquals(getTestCockpitParams(), CockpitManager.getParamsCopy())
        assertEquals(getTestCockpitParams(), CockpitManager.getDefaultParamsCopy())
    }

    @AfterTest
    fun deleteFileAndDirectory() {
        val file = File("$DIRECTORY_PATH/savedCockpit.yml")
        val fileResult = file.delete()
        System.out.println("Deleted file $file: $fileResult")
        val directory = File(DIRECTORY_PATH)
        val directoryResult = directory.delete()
        System.out.println("Deleted directory $directory: $directoryResult")
    }

    private fun getTestCockpitParams(): MutableList<CockpitParam<Any>> {
        val testParams: MutableList<CockpitParam<Any>> = mutableListOf()

        testParams.add(CockpitParam("doubleParam", 3.0))
        testParams.add(CockpitParam("booleanParam", false))
        testParams.add(CockpitParam("stringParam", "testValue"))
        testParams.add(CockpitParam("integerParam", 2))

        return testParams
    }

    companion object {
        private const val DIRECTORY_PATH = "data"
    }
}