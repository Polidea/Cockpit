package com.polidea.cockpit.utils

import android.content.Context
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.Param
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.NotifiableParam
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
        FileUtils.cockpitYamlFileManager = cockpitYamlFileManager

        every { cockpitYamlFileManager.readInputParams() } returns getTestCockpitParams()
        every { cockpitYamlFileManager.readSavedParams() } returns emptyList()
    }

    @Test
    fun saveAndReadCockpit() {
        getTestCockpitParams().forEach { CockpitManager.addParam(it) }
        FileUtils.saveCockpitAsYaml()
        CockpitManager.clear()
        FileUtils.loadCockpitParams()
        assertEquals(getTestCockpitParams(), CockpitManager.params)
        assertEquals(getTestCockpitParams(), CockpitManager.defaultParams)
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

    private fun getTestCockpitParams(): MutableList<Param<*>> {
        val testParams: MutableList<Param<*>> = mutableListOf()

        testParams.add(NotifiableParam("doubleParam", 3.0, null, null))
        testParams.add(NotifiableParam("booleanParam", false, null, null))
        testParams.add(NotifiableParam("stringParam", "testValue", null, null))
        testParams.add(NotifiableParam("integerParam", 2, null, null))

        return testParams
    }

    companion object {
        private const val DIRECTORY_PATH = "data"
    }
}