package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitListType
import kotlin.test.Test
import kotlin.test.assertEquals

class ReleaseCockpitGeneratorTest {

    private val cockpitGenerator = ReleaseCockpitGenerator()

    @Test
    fun createGetterMethodSpecForDoubleParamTest() {
        val doubleGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("doubleParam", 3.0)

        val expectedDoubleGetterMethodSpecString = """
            |public static double getDoubleParam() {
            |  return 3.0;
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("integerParam", 2)

        val expectedIntegerGetterMethodSpecString = """
            |public static int getIntegerParam() {
            |  return 2;
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("booleanParam", false)

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean isBooleanParam() {
            |  return false;
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("stringParam", "testValue")

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getStringParam() {
            |  return "testValue";
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSelectedValueGetterMethodSpecForParamTest() {
        val arrayList = listOf("a", "b", "c")
        val funSpec = cockpitGenerator.createSelectedValueGetterMethodSpecForParam(CockpitParam("name", CockpitListType(arrayList, 1)))

        val expectedFunSpecString = """
            |public static java.lang.String getNameSelectedValue() {
            |  return "b";
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }
}