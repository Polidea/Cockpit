package com.polidea.cockpitplugin.generator

import com.polidea.cockpitplugin.model.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ReleaseCockpitGeneratorTest {

    private val cockpitGenerator = ReleaseCockpitGenerator()

    @Test
    fun createGetterMethodSpecForDoubleParamTest() {
        val doubleGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(DoubleParam("doubleParam", 3.0))

        val expectedDoubleGetterMethodSpecString = """
            |public static double getdoubleParam() {
            |  return 3.0;
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedIntegerGetterMethodSpecString = """
            |public static int getintegerParam() {
            |  return 2;
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean getbooleanParam() {
            |  return false;
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getstringParam() {
            |  return "testValue";
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetAllCockpitParamsMethodTest() {
        val funSpec = cockpitGenerator.createGetAllCockpitParamsMethod(getTestParams())

        val expectedFunSpecString = """
            |public static java.util.List<com.polidea.cockpit.manager.CockpitParam> getAllCockpitParams() {
            |  java.util.List<com.polidea.cockpit.manager.CockpitParam> cockpit = new java.util.ArrayList<>();
            |  cockpit.add(new CockpitParam("doubleParam", Double.class, 3.0));
            |  cockpit.add(new CockpitParam("booleanParam", Boolean.class, false));
            |  cockpit.add(new CockpitParam("stringParam", String.class, "testValue"));
            |  cockpit.add(new CockpitParam("integerParam", Integer.class, 2));
            |  return cockpit;
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