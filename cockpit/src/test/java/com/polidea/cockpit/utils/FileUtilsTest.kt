package com.polidea.cockpit.utils

import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
import io.mockk.every
import io.mockk.mockk
import java.io.File
import java.util.stream.Collectors
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilsTest {

    private val inputParamsProvider: InputParamsProvider = mockk()

    init {
        File(DIRECTORY_PATH).mkdirs()
        FileUtils.init(DIRECTORY_PATH)

        FileUtils.inputParamsProvider = inputParamsProvider

        every { inputParamsProvider.getInputParams() } returns getTestCockpitParams().stream().collect(Collectors.toMap(CockpitParam::name, CockpitParam::value))
    }

    @Test
    fun saveAndReadCockpit() {
        getTestCockpitParams().forEach { CockpitManager.addParam(it) }
        FileUtils.saveCockpitAsYaml()
        CockpitManager.clear()
        FileUtils.readCockpitFromFile()
        assertEquals(getTestCockpitParams(), CockpitManager.params)
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

        testParams.add(CockpitParam("doubleParam", Double::class.javaObjectType, 3.0))
        testParams.add(CockpitParam("booleanParam", Boolean::class.javaObjectType, false))
        testParams.add(CockpitParam("stringParam", String::class.javaObjectType, "testValue"))
        testParams.add(CockpitParam("integerParam", Int::class.javaObjectType, 2))

        return testParams
    }

    companion object {
        private const val DIRECTORY_PATH = "data"
    }
}