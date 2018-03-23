package com.polidea.cockpitplugin.generator

import com.polidea.cockpitplugin.model.*
import kotlin.test.Test
import kotlin.test.assertEquals


class DebugCockpitGeneratorTest {

    private val cockpitGenerator = DebugCockpitGenerator()

    @Test
    fun createGetterMethodSpecForDoubleParamTest() {
        val doubleGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(DoubleParam("doubleParam", 3.0))

        val expectedDoubleGetterMethodSpecString = """
            |public static double getdoubleParam() {
            |  return (double) com.polidea.cockpit.manager.CockpitManager.getInstance().getParamValue("doubleParam");
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForDoubleParamTest() {
        val doubleSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam(DoubleParam("doubleParam", 3.0))

        val expectedDoubleSetterMethodSpecString = """
            |public static void setdoubleParam(double doubleParam) {
            |  com.polidea.cockpit.manager.CockpitManager.getInstance().setParamValue("doubleParam", doubleParam);
            |}"""
        assertEquals(expectedDoubleSetterMethodSpecString.trimMargin(), doubleSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedIntegerGetterMethodSpecString = """
            |public static int getintegerParam() {
            |  return (int) com.polidea.cockpit.manager.CockpitManager.getInstance().getParamValue("integerParam");
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForIntParamTest() {
        val intSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam(IntegerParam("integerParam", 2))

        val expectedDoubleSetterMethodSpecString = """
            |public static void setintegerParam(int integerParam) {
            |  com.polidea.cockpit.manager.CockpitManager.getInstance().setParamValue("integerParam", integerParam);
            |}"""
        assertEquals(expectedDoubleSetterMethodSpecString.trimMargin(), intSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean getbooleanParam() {
            |  return (boolean) com.polidea.cockpit.manager.CockpitManager.getInstance().getParamValue("booleanParam");
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForBooleanParamTest() {
        val booleanSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam(BooleanParam("booleanParam", false))

        val expectedBooleanSetterMethodSpecString = """
            |public static void setbooleanParam(boolean booleanParam) {
            |  com.polidea.cockpit.manager.CockpitManager.getInstance().setParamValue("booleanParam", booleanParam);
            |}"""
        assertEquals(expectedBooleanSetterMethodSpecString.trimMargin(), booleanSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getstringParam() {
            |  return (java.lang.String) com.polidea.cockpit.manager.CockpitManager.getInstance().getParamValue("stringParam");
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForStringParamTest() {
        val stringSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam(StringParam("stringParam", "testValue"))

        val expectedStringSetterMethodSpecString = """
            |public static void setstringParam(java.lang.String stringParam) {
            |  com.polidea.cockpit.manager.CockpitManager.getInstance().setParamValue("stringParam", stringParam);
            |}"""
        assertEquals(expectedStringSetterMethodSpecString.trimMargin(), stringSetterMethodSpec.toString().trimMargin())
    }


    @Test
    fun createGetAllCockpitParamsMethodTest() {
        val funSpec = cockpitGenerator.createGetAllCockpitParamsMethod()

        val expectedFunSpecString = """
            |public static java.util.List<com.polidea.cockpit.manager.CockpitParam> getAllCockpitParams() {
            |  return com.polidea.cockpit.manager.CockpitManager.getInstance().getParams();
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun createInitCockpitMethodTest() {
        val params = getTestParams()
        System.out.println(params.toString())

        val funSpec = cockpitGenerator.createInitCockpitMethod(params)

        val expectedFunSpecString = """
            |private static void initializeCockpit() {
            |  CockpitManager.getInstance().addParam(new CockpitParam("doubleParam", Double.class, 3.0));
            |  CockpitManager.getInstance().addParam(new CockpitParam("booleanParam", Boolean.class, false));
            |  CockpitManager.getInstance().addParam(new CockpitParam("stringParam", String.class, "testValue"));
            |  CockpitManager.getInstance().addParam(new CockpitParam("integerParam", Integer.class, 2));
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun generateShowCockpitMethodTest() {
        val funSpec = cockpitGenerator.generateShowCockpitMethod()

        val expectedFunSpecString = """
            |public static void showCockpit(android.content.Context context) {
            |  android.content.Intent intent = new android.content.Intent(context, com.polidea.cockpit.activity.CockpitActivity .class);
            |  context.startActivity(intent);
            |}"""

        System.out.println("expected: " + expectedFunSpecString.trimMargin())
        System.out.println("given   : " + funSpec.toString().trimMargin())
        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun generateHideCockpitMethodTest() {
        val funSpec = cockpitGenerator.generateHideCockpitMethod()

        val expectedFunSpecString = """
            |public static void hideCockpit(com.polidea.cockpit.activity.CockpitActivity cockpitActivity) {
            |  cockpitActivity.finish();
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