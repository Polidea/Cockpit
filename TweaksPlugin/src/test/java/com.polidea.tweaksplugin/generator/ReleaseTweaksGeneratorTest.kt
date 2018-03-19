package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ReleaseTweaksGeneratorTest {

    private val tweaksGenerator = ReleaseTweaksGenerator()

    @Test
    fun createGetterMethodSpecForDoubleParamTest() {
        val doubleGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(DoubleParam("doubleParam", 3.0))

        val expectedDoubleGetterMethodSpecString = """
            |public static double getdoubleParam() {
            |  return 3.0;
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedIntegerGetterMethodSpecString = """
            |public static int getintegerParam() {
            |  return 2;
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean getbooleanParam() {
            |  return false;
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = tweaksGenerator.createGetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getstringParam() {
            |  return "testValue";
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetAllTweaksMethodTest() {
        val funSpec = tweaksGenerator.createGetAllTweaksMethod(getTestParams())

        val expectedFunSpecString = """
            |public static java.util.List<com.polidea.androidtweaks.manager.TweakParam> getAllTweaks() {
            |  java.util.List<com.polidea.androidtweaks.manager.TweakParam> tweaks = new java.util.ArrayList<>();
            |  tweaks.add(new TweakParam("doubleParam", Double.class, 3.0));
            |  tweaks.add(new TweakParam("booleanParam", Boolean.class, false));
            |  tweaks.add(new TweakParam("stringParam", String.class, "testValue"));
            |  tweaks.add(new TweakParam("integerParam", Integer.class, 2));
            |  return tweaks;
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