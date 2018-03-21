package com.polidea.cockpit.utils

import android.content.Context
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.manager.CockpitParam
import io.mockk.mockk
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilsTest {

    private var context = mockk<Context>(relaxed = true)
    private var fileUtils: FileUtils = FileUtils(context)

    private val cockpitManager = CockpitManager.getInstance()
    private val params = getTestCockpitParams()

    init {
        fileUtils.cockpitManager = cockpitManager
        fileUtils.savedCockpitFilePath = "test.yml"
        cockpitManager.params = params
    }

    @Test
    fun saveAndReadCockpit() {
        fileUtils.saveCockpitAsYaml()
        fileUtils.readCockpitFromFile()
        assertEquals(getTestCockpitParams(), cockpitManager.params)
    }

    @AfterTest
    fun deleteFile() {
        val f = File(fileUtils.savedCockpitFilePath)
        val result = f.delete()
        System.out.println("Deleted file $f: $result")
    }

    private fun getTestCockpitParams(): MutableList<CockpitParam> {
        val testParams: MutableList<CockpitParam> = mutableListOf()

        testParams.add(CockpitParam("doubleParam", Double::class.java, 3.0))
        testParams.add(CockpitParam("booleanParam", Boolean::class.java, false))
        testParams.add(CockpitParam("stringParam", String::class.java, "testValue"))
        testParams.add(CockpitParam("integerParam", Int::class.java, 2))

        return testParams
    }
}