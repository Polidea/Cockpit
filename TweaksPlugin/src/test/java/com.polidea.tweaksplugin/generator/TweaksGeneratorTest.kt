package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import kotlin.test.Test
import kotlin.test.assertEquals


class TweaksGeneratorTest {

    private val tweaksGenerator: TweaksGenerator = TweaksGenerator()

    @Test
    fun createPropertySpecForDoubleParamTest() {
        val doublePropertySpec = tweaksGenerator.createPropertySpecForParam(DoubleParam("doubleParam", 3.0), Double::class)

        val expectedDoublePropertySpecString = """
            |@kotlin.jvm.JvmStatic
            |public var doubleParam: kotlin.Double
            |    get() = TweaksManager.getInstance().getParamValue("doubleParam") as Double
            |    set(value) {
            |        TweaksManager.getInstance().setParamValue("doubleParam", value)
            |    }"""

        assertEquals(expectedDoublePropertySpecString.trimMargin(), doublePropertySpec.toString().trimMargin())
    }

    @Test
    fun createPropertySpecForIntParamTest() {
        val integerPropertySpec = tweaksGenerator.createPropertySpecForParam(IntegerParam("integerParam", 2), Int::class)

        val expectedIntPropertySpecString = """
            |@kotlin.jvm.JvmStatic
            |public var integerParam: kotlin.Int
            |    get() = TweaksManager.getInstance().getParamValue("integerParam") as Int
            |    set(value) {
            |        TweaksManager.getInstance().setParamValue("integerParam", value)
            |    }"""

        assertEquals(expectedIntPropertySpecString.trimMargin(), integerPropertySpec.toString().trimMargin())
    }

    @Test
    fun createPropertySpecForBooleanParamTest() {
        val booleanPropertySpec = tweaksGenerator.createPropertySpecForParam(BooleanParam("booleanParam", false), Boolean::class)

        val expectedBooleanPropertySpecString = """
            |@kotlin.jvm.JvmStatic
            |public var booleanParam: kotlin.Boolean
            |    get() = TweaksManager.getInstance().getParamValue("booleanParam") as Boolean
            |    set(value) {
            |        TweaksManager.getInstance().setParamValue("booleanParam", value)
            |    }"""

        assertEquals(expectedBooleanPropertySpecString.trimMargin(), booleanPropertySpec.toString().trimMargin())
    }

    @Test
    fun createPropertySpecForStringParamTest() {
        val stringPropertySpec = tweaksGenerator.createPropertySpecForParam(StringParam("stringParam", "testValue"), String::class)

        val expectedStringPropertySpecString = """
            |@kotlin.jvm.JvmStatic
            |public var stringParam: kotlin.String
            |    get() = TweaksManager.getInstance().getParamValue("stringParam") as String
            |    set(value) {
            |        TweaksManager.getInstance().setParamValue("stringParam", value)
            |    }"""

        assertEquals(expectedStringPropertySpecString.trimMargin(), stringPropertySpec.toString().trimMargin())
    }

    @Test
    fun createGetAllTweaksMethodTest() {
        val funSpec = tweaksGenerator.createGetAllTweaksMethod()

        val expectedFunSpecString = """
            |@kotlin.jvm.JvmStatic
            |fun getAllTweaks(): kotlin.collections.List<com.polidea.androidtweaks.manager.TweakParam> = TweaksManager.getInstance().params"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun createInitTweaksMethodTest() {
        val params = getTestParams()
        System.out.println(params.toString())

        val funSpec = tweaksGenerator.createInitTweaksMethod(params)

        val expectedFunSpecString = """
            |private fun initializeTweaks() {
            |    TweaksManager.getInstance().addParam(TweakParam("doubleParam", Double::class, 3.0))
            |    TweaksManager.getInstance().addParam(TweakParam("booleanParam", Boolean::class, false))
            |    TweaksManager.getInstance().addParam(TweakParam("stringParam", String::class, "testValue"))
            |    TweaksManager.getInstance().addParam(TweakParam("integerParam", Int::class, 2))
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun generateShowTweaksMethodTest() {
        val funSpec = tweaksGenerator.generateShowTweaksMethod()

        val expectedFunSpecString = """
            |@kotlin.jvm.JvmStatic
            |fun showTweaks(context: android.content.Context) {
            |    val intent = Intent(context, TweaksActivity::class.java)
            |    context.startActivity(intent)
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun generateHideTweaksMethodTest() {
        val funSpec = tweaksGenerator.generateHideTweaksMethod()

        val expectedFunSpecString = """
            |@kotlin.jvm.JvmStatic
            |fun hideTweaks(tweaksActivity: com.polidea.androidtweaks.activity.TweaksActivity) {
            |    tweaksActivity.finish()
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    private fun getTestParams(): List<Param<*>> {
        val testParams: MutableList<Param<*>> = mutableListOf()

        testParams.add(DoubleParam("doubleParam", 3.0))
        testParams.add(BooleanParam("booleanParam", false))
        testParams.add(StringParam("stringParam", "testValue"))
        testParams.add(IntegerParam("integerParam", 2))

        return testParams
    }
}