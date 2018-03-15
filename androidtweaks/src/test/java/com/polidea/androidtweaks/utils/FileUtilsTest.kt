package com.polidea.androidtweaks.utils

import android.content.Context
import com.polidea.androidtweaks.manager.TweakParam
import io.mockk.mockk
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilsTest {

    private var context = mockk<Context>(relaxed = true)
    private var fileUtils: FileUtils = FileUtils(context)

    private val tweaksManager = TweaksManager.getInstance()
    private val params = getTestTweaksParams()

    init {
        fileUtils.tweaksManager = tweaksManager
        fileUtils.savedTweaksFilePath = "test.yml"
        tweaksManager.params = params
    }

    @Test
    fun saveAndReadTweaks() {
        fileUtils.saveTweaksAsYaml()
        fileUtils.readTweaksFromFile()
        assertEquals(getTestTweaksParams(), tweaksManager.params)
    }

    @AfterTest
    fun deleteFile() {
        val f = File(fileUtils.savedTweaksFilePath)
        val result = f.delete()
        System.out.println("Deleted file $f: $result")
    }

    private fun getTestTweaksParams(): MutableList<TweakParam> {
        val testParams: MutableList<TweakParam> = mutableListOf()

        testParams.add(TweakParam("doubleParam", Double::class.java, 3.0))
        testParams.add(TweakParam("booleanParam", Boolean::class.java, false))
        testParams.add(TweakParam("stringParam", String::class.java, "testValue"))
        testParams.add(TweakParam("integerParam", Int::class.java, 2))

        return testParams
    }
}