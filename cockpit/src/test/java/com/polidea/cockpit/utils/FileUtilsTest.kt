package com.polidea.cockpit.utils

import android.content.Context
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
import io.mockk.classMockk
import io.mockk.every
import io.mockk.mockk
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilsTest {

    private var fileUtils: FileUtils = FileUtils.getInstance()

    private val cockpitManager = CockpitManager.getInstance()
    private val params = getTestCockpitParams()

    init {
        val contextMock = mockk<Context>(relaxed = true)
        File(DIRECTORY_PATH).mkdirs()
        every { contextMock.filesDir.path } returns DIRECTORY_PATH
        FileUtils.init(contextMock)
        fileUtils.cockpitManager = cockpitManager
        cockpitManager.params = params
    }

    @Test
    fun saveAndReadCockpit() {
        fileUtils.saveCockpitAsYaml()
        fileUtils.readCockpitFromFile()
        assertEquals(getTestCockpitParams(), cockpitManager.params)
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

    private fun getTestCockpitParams(): MutableList<CockpitParam> {
        val testParams: MutableList<CockpitParam> = mutableListOf()

        testParams.add(CockpitParam("doubleParam", Double::class.java, 3.0))
        testParams.add(CockpitParam("booleanParam", Boolean::class.java, false))
        testParams.add(CockpitParam("stringParam", String::class.java, "testValue"))
        testParams.add(CockpitParam("integerParam", Int::class.java, 2))

        return testParams
    }

    companion object {
        private const val DIRECTORY_PATH = "data"
    }
}