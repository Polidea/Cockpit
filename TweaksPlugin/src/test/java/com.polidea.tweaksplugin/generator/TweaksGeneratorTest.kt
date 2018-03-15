package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import kotlin.test.Test
import kotlin.test.assertEquals


class TweaksGeneratorTest {

    private val tweaksGenerator: TweaksGenerator = TweaksGenerator()

    @Test
    fun createGetterMethodSpecForDoubleParamTest() {
        val doubleGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(DoubleParam("doubleParam", 3.0))

        val expectedDoubleGetterMethodSpecString = """
            |public static double getdoubleParam() {
            |  return (double) com.polidea.androidtweaks.manager.TweaksManager.getInstance().getParamValue("doubleParam");
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForDoubleParamTest() {
        val doubleSetterMethodSpec = tweaksGenerator.createSetterMethodSpecForParam(DoubleParam("doubleParam", 3.0))

        val expectedDoubleSetterMethodSpecString = """
            |public static void setdoubleParam(double doubleParam) {
            |  com.polidea.androidtweaks.manager.TweaksManager.getInstance().setParamValue("doubleParam", doubleParam);
            |}"""
        assertEquals(expectedDoubleSetterMethodSpecString.trimMargin(), doubleSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedIntegerGetterMethodSpecString = """
            |public static int getintegerParam() {
            |  return (int) com.polidea.androidtweaks.manager.TweaksManager.getInstance().getParamValue("integerParam");
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForIntParamTest() {
        val intSetterMethodSpec = tweaksGenerator.createSetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedDoubleSetterMethodSpecString = """
            |public static void setintegerParam(int integerParam) {
            |  com.polidea.androidtweaks.manager.TweaksManager.getInstance().setParamValue("integerParam", integerParam);
            |}"""
        assertEquals(expectedDoubleSetterMethodSpecString.trimMargin(), intSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean getbooleanParam() {
            |  return (boolean) com.polidea.androidtweaks.manager.TweaksManager.getInstance().getParamValue("booleanParam");
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForBooleanParamTest() {
        val booleanSetterMethodSpec = tweaksGenerator.createSetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanSetterMethodSpecString = """
            |public static void setbooleanParam(boolean booleanParam) {
            |  com.polidea.androidtweaks.manager.TweaksManager.getInstance().setParamValue("booleanParam", booleanParam);
            |}"""
        assertEquals(expectedBooleanSetterMethodSpecString.trimMargin(), booleanSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getstringParam() {
            |  return (java.lang.String) com.polidea.androidtweaks.manager.TweaksManager.getInstance().getParamValue("stringParam");
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForStringParamTest() {
        val stringSetterMethodSpec = tweaksGenerator.createSetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringSetterMethodSpecString = """
            |public static void setstringParam(java.lang.String stringParam) {
            |  com.polidea.androidtweaks.manager.TweaksManager.getInstance().setParamValue("stringParam", stringParam);
            |}"""
        assertEquals(expectedStringSetterMethodSpecString.trimMargin(), stringSetterMethodSpec.toString().trimMargin())
    }


    @Test
    fun createGetAllTweaksMethodTest() {
        val funSpec = tweaksGenerator.createGetAllTweaksMethod()

        val expectedFunSpecString = """
            |public static java.util.List<com.polidea.androidtweaks.manager.TweakParam> getAllTweaks() {
            |  return com.polidea.androidtweaks.manager.TweaksManager.getInstance().getParams();
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun createInitTweaksMethodTest() {
        val params = getTestParams()
        System.out.println(params.toString())

        val funSpec = tweaksGenerator.createInitTweaksMethod(params)

        val expectedFunSpecString = """
            |private static void initializeTweaks() {
            |  TweaksManager.getInstance().addParam(new TweakParam("doubleParam", Double.class, 3.0));
            |  TweaksManager.getInstance().addParam(new TweakParam("booleanParam", Boolean.class, false));
            |  TweaksManager.getInstance().addParam(new TweakParam("stringParam", String.class, "testValue"));
            |  TweaksManager.getInstance().addParam(new TweakParam("integerParam", Integer.class, 2));
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun generateShowTweaksMethodTest() {
        val funSpec = tweaksGenerator.generateShowTweaksMethod()

        val expectedFunSpecString = """
            |public static void showTweaks(android.content.Context context) {
            |  android.content.Intent intent = new android.content.Intent(context, com.polidea.androidtweaks.activity.TweaksActivity .class);
            |  context.startActivity(intent);
            |}"""

        System.out.println("expected: " + expectedFunSpecString.trimMargin())
        System.out.println("given   : " + funSpec.toString().trimMargin())
        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun generateHideTweaksMethodTest() {
        val funSpec = tweaksGenerator.generateHideTweaksMethod()

        val expectedFunSpecString = """
            |public static void hideTweaks(com.polidea.androidtweaks.activity.TweaksActivity tweaksActivity) {
            |  tweaksActivity.finish();
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