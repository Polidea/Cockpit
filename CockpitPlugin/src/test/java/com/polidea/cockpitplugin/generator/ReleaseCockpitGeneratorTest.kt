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
            |public static double getDoubleParam() {
            |  return 3.0;
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedIntegerGetterMethodSpecString = """
            |public static int getIntegerParam() {
            |  return 2;
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean isBooleanParam() {
            |  return false;
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getStringParam() {
            |  return "testValue";
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }
}