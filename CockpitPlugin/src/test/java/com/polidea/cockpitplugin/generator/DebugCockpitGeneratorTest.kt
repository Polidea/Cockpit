package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitListType
import com.polidea.cockpit.core.type.CockpitColor
import kotlin.test.Test
import kotlin.test.assertEquals


class DebugCockpitGeneratorTest {

    private val cockpitGenerator = DebugCockpitGenerator()

    @Test
    fun createGetterMethodSpecForDoubleParamTest() {
        val doubleGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("doubleParam", 3.0)

        val expectedDoubleGetterMethodSpecString = """
            |public static double getDoubleParam() {
            |  return (double) com.polidea.cockpit.manager.CockpitManager.INSTANCE.getParamValue("doubleParam");
            |}"""
        assertEquals(expectedDoubleGetterMethodSpecString.trimMargin(), doubleGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForDoubleParamTest() {
        val doubleSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam("doubleParam", 3.0)

        val expectedDoubleSetterMethodSpecString = """
            |public static void setDoubleParam(double doubleParam) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.setParamValue("doubleParam", doubleParam);
            |}"""
        assertEquals(expectedDoubleSetterMethodSpecString.trimMargin(), doubleSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForIntParamTest() {
        val intGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("integerParam", 2)

        val expectedIntegerGetterMethodSpecString = """
            |public static int getIntegerParam() {
            |  return (int) com.polidea.cockpit.manager.CockpitManager.INSTANCE.getParamValue("integerParam");
            |}"""
        assertEquals(expectedIntegerGetterMethodSpecString.trimMargin(), intGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForIntParamTest() {
        val intSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam("integerParam", 2)

        val expectedDoubleSetterMethodSpecString = """
            |public static void setIntegerParam(int integerParam) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.setParamValue("integerParam", integerParam);
            |}"""
        assertEquals(expectedDoubleSetterMethodSpecString.trimMargin(), intSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForBooleanParamTest() {
        val booleanGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("booleanParam", false)

        val expectedBooleanGetterMethodSpecString = """
            |public static boolean isBooleanParam() {
            |  return (boolean) com.polidea.cockpit.manager.CockpitManager.INSTANCE.getParamValue("booleanParam");
            |}"""
        assertEquals(expectedBooleanGetterMethodSpecString.trimMargin(), booleanGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForBooleanParamTest() {
        val booleanSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam("booleanParam", false)

        val expectedBooleanSetterMethodSpecString = """
            |public static void setBooleanParam(boolean booleanParam) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.setParamValue("booleanParam", booleanParam);
            |}"""
        assertEquals(expectedBooleanSetterMethodSpecString.trimMargin(), booleanSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createGetterMethodSpecForStringParamTest() {
        val stringGetterMethodSpec = cockpitGenerator.createGetterMethodSpecForParam("stringParam", "testValue")

        val expectedStringGetterMethodSpecString = """
            |public static java.lang.String getStringParam() {
            |  return (java.lang.String) com.polidea.cockpit.manager.CockpitManager.INSTANCE.getParamValue("stringParam");
            |}"""
        assertEquals(expectedStringGetterMethodSpecString.trimMargin(), stringGetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createSetterMethodSpecForStringParamTest() {
        val stringSetterMethodSpec = cockpitGenerator.createSetterMethodSpecForParam("stringParam", "testValue")

        val expectedStringSetterMethodSpecString = """
            |public static void setStringParam(java.lang.String stringParam) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.setParamValue("stringParam", stringParam);
            |}"""
        assertEquals(expectedStringSetterMethodSpecString.trimMargin(), stringSetterMethodSpec.toString().trimMargin())
    }

    @Test
    fun createAddParamChangeListenerMethodSpecForBooleanParamTest() {
        val booleanParamChangeListenerMethodSpec = cockpitGenerator.createAddPropertyChangeListenerMethodSpecForParam("boolParam", false)

        val expectedBooleanParamChangeListenerMethodSpecString = """
            |public static void addOnBoolParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.Boolean> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addOnParamChangeListener("boolParam", listener);
            |}"""
        assertEquals(expectedBooleanParamChangeListenerMethodSpecString.trimMargin(), booleanParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createAddParamChangeListenerMethodSpecForIntParamTest() {
        val integerParamChangeListenerMethodSpec = cockpitGenerator.createAddPropertyChangeListenerMethodSpecForParam("intParam", 0)

        val expectedIntegerParamChangeListenerMethodSpecString = """
            |public static void addOnIntParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.Integer> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addOnParamChangeListener("intParam", listener);
            |}"""
        assertEquals(expectedIntegerParamChangeListenerMethodSpecString.trimMargin(), integerParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createAddParamChangeListenerMethodSpecForDoubleParamTest() {
        val doubleParamChangeListenerMethodSpec = cockpitGenerator.createAddPropertyChangeListenerMethodSpecForParam("doubleParam", .0)

        val expectedDoubleParamChangeListenerMethodSpecString = """
            |public static void addOnDoubleParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.Double> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addOnParamChangeListener("doubleParam", listener);
            |}"""
        assertEquals(expectedDoubleParamChangeListenerMethodSpecString.trimMargin(), doubleParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createAddParamChangeListenerMethodSpecForStringParamTest() {
        val stringParamChangeListenerMethodSpec = cockpitGenerator.createAddPropertyChangeListenerMethodSpecForParam("stringParam", "")

        val expectedStringParamChangeListenerMethodSpecString = """
            |public static void addOnStringParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.String> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addOnParamChangeListener("stringParam", listener);
            |}"""
        assertEquals(expectedStringParamChangeListenerMethodSpecString.trimMargin(), stringParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createAddActionRequestCallbackMethodSpecForStringParamTest() {
        val actionCallbackMethodSpec = cockpitGenerator.createAddActionRequestCallbackMethodSpecForParam(CockpitParam("value", CockpitAction("show")))

        val expectedActionRequestCallbackMethodSpecString = """
            |public static void addValueActionRequestCallback(
            |    com.polidea.cockpit.event.ActionRequestCallback callback) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addActionRequestCallback("value", callback);
            |}"""
        assertEquals(expectedActionRequestCallbackMethodSpecString.trimMargin(), actionCallbackMethodSpec.toString().trimMargin())
    }

    @Test
    fun createAddParamChangeListenerMethodSpecForCockpitColorParamTest() {
        val colorParamChangeListenerMethodSpec = cockpitGenerator.createAddPropertyChangeListenerMethodSpecForColorParam("colorParam", CockpitColor("#112233"))

        val expectedStringParamChangeListenerMethodSpecString = """
            |public static void addOnColorParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.String> listener) {
            |  com.polidea.cockpit.event.PropertyChangeListener<com.polidea.cockpit.core.type.CockpitColor> colorParamListener = new com.polidea.cockpit.mapper.MappingPropertyChangeListener<com.polidea.cockpit.core.type.CockpitColor, java.lang.String>(listener, cockpitColorMapper);
            |  colorListenerMap.put(listener, colorParamListener);
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addOnParamChangeListener("colorParam", colorParamListener);
            |}"""
        assertEquals(expectedStringParamChangeListenerMethodSpecString.trimMargin(), colorParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createRemoveParamChangeListenerMethodSpecForBooleanParamTest() {
        val booleanParamChangeListenerMethodSpec = cockpitGenerator.createRemovePropertyChangeListenerMethodSpecForParam("boolParam", false)

        val expectedBooleanParamChangeListenerMethodSpecString = """
            |public static void removeOnBoolParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.Boolean> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeOnParamChangeListener("boolParam", listener);
            |}"""
        assertEquals(expectedBooleanParamChangeListenerMethodSpecString.trimMargin(), booleanParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createRemoveParamChangeListenerMethodSpecForIntParamTest() {
        val integerParamChangeListenerMethodSpec = cockpitGenerator.createRemovePropertyChangeListenerMethodSpecForParam("intParam", 0)

        val expectedIntegerParamChangeListenerMethodSpecString = """
            |public static void removeOnIntParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.Integer> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeOnParamChangeListener("intParam", listener);
            |}"""
        assertEquals(expectedIntegerParamChangeListenerMethodSpecString.trimMargin(), integerParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createRemoveParamChangeListenerMethodSpecForDoubleParamTest() {
        val doubleParamChangeListenerMethodSpec = cockpitGenerator.createRemovePropertyChangeListenerMethodSpecForParam("doubleParam", .0)

        val expectedDoubleParamChangeListenerMethodSpecString = """
            |public static void removeOnDoubleParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.Double> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeOnParamChangeListener("doubleParam", listener);
            |}"""
        assertEquals(expectedDoubleParamChangeListenerMethodSpecString.trimMargin(), doubleParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createRemoveParamChangeListenerMethodSpecForStringParamTest() {
        val stringParamChangeListenerMethodSpec = cockpitGenerator.createRemovePropertyChangeListenerMethodSpecForParam("stringParam", "")

        val expectedStringParamChangeListenerMethodSpecString = """
            |public static void removeOnStringParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.String> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeOnParamChangeListener("stringParam", listener);
            |}"""
        assertEquals(expectedStringParamChangeListenerMethodSpecString.trimMargin(), stringParamChangeListenerMethodSpec.toString().trimMargin())
    }

    @Test
    fun createRemoveActionRequestCallbackMethodSpecForStringParamTest() {
        val actionCallbackMethodSpec = cockpitGenerator.createRemoveActionRequestCallbackMethodSpecForParam(CockpitParam("value", CockpitAction("show")))

        val expectedActionRequestCallbackMethodSpecString = """
            |public static void removeValueActionRequestCallback(
            |    com.polidea.cockpit.event.ActionRequestCallback callback) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeActionRequestCallback("value", callback);
            |}"""
        assertEquals(expectedActionRequestCallbackMethodSpecString.trimMargin(), actionCallbackMethodSpec.toString().trimMargin())
    }
    
    @Test
    fun createRemoveParamChangeListenerMethodSpecForColorParamTest() {
        val colorParamChangeListenerMethodSpec = cockpitGenerator.createRemovePropertyChangeListenerMethodSpecForColorParam("colorParam", CockpitColor("#112233"))

        val expectedColorParamChangeListenerMethodSpecString = """
            |public static void removeOnColorParamChangeListener(
            |    com.polidea.cockpit.event.PropertyChangeListener<java.lang.String> listener) {
            |  com.polidea.cockpit.event.PropertyChangeListener<com.polidea.cockpit.core.type.CockpitColor> colorParamListener = colorListenerMap.get(listener);
            |  colorListenerMap.remove(listener, colorParamListener);
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeOnParamChangeListener("colorParam", colorParamListener);
            |}"""

        assertEquals(expectedColorParamChangeListenerMethodSpecString.trimMargin(), colorParamChangeListenerMethodSpec.toString().trimMargin())
    }


    @Test
    fun generateShowCockpitMethodTest() {
        val funSpec = cockpitGenerator.generateShowCockpitMethod()

        val expectedFunSpecString = """
            |public static void showCockpit(android.support.v4.app.FragmentManager fragmentManager) {
            |  if (fragmentManager.findFragmentByTag("Cockpit") == null) {
            |    com.polidea.cockpit.paramsedition.CockpitDialog cockpitDialog = com.polidea.cockpit.paramsedition.CockpitDialog.Companion.newInstance();
            |    cockpitDialog.show(fragmentManager, "Cockpit");
            |  }
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun createAddSelectionChangeListenerMethodSpecForParamTest() {
        val arrayList = listOf("a", "b", "c")
        val funSpec = cockpitGenerator.createAddSelectionChangeListenerMethodSpecForParam(CockpitParam("name", CockpitListType(arrayList, 1)))

        val expectedFunSpecString = """
            |public static void addNameSelectionChangeListener(
            |    com.polidea.cockpit.event.SelectionChangeListener<java.lang.String> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.addSelectionChangeListener("name", listener);
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun createRemoveSelectionChangeListenerMethodSpecForParamTest() {
        val funSpec = cockpitGenerator.createRemoveSelectionChangeListenerMethodSpecForParam(CockpitParam("name", CockpitListType(listOf("a", "b", "c"), 1)))

        val expectedFunSpecString = """
            |public static void removeNameSelectionChangeListener(
            |    com.polidea.cockpit.event.SelectionChangeListener<java.lang.String> listener) {
            |  com.polidea.cockpit.manager.CockpitManager.INSTANCE.removeSelectionChangeListener("name", listener);
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }

    @Test
    fun createColorListenerMapFieldSpecTest() {
        val fieldSpec = cockpitGenerator.createColorListenerMapFieldSpec()

        val expectedFieldSpecString = """
            |private static final java.util.Map<com.polidea.cockpit.event.PropertyChangeListener<java.lang.String>, com.polidea.cockpit.event.PropertyChangeListener<com.polidea.cockpit.core.type.CockpitColor>> colorListenerMap = new java.util.HashMap<>();
            """
        assertEquals(expectedFieldSpecString.trimMargin(), fieldSpec.toString().trimMargin())
    }

    @Test
    fun createCockpitColorMapperFieldSpecTest() {
        val fieldSpec = cockpitGenerator.createColorMapperFieldSpec()

        val expectedFieldSpecString = """
            |private static final com.polidea.cockpit.mapper.CockpitColorMapper cockpitColorMapper = new com.polidea.cockpit.mapper.CockpitColorMapper();
            """
        assertEquals(expectedFieldSpecString.trimMargin(), fieldSpec.toString().trimMargin())
    }

    @Test
    fun createSelectedValueGetterMethodSpecForParamTest() {
        val arrayList = listOf("a", "b", "c")
        val funSpec = cockpitGenerator.createSelectedValueGetterMethodSpecForParam(CockpitParam("name", CockpitListType(arrayList, 1)))

        val expectedFunSpecString = """
            |public static java.lang.String getNameSelectedValue() {
            |  return com.polidea.cockpit.manager.CockpitManager.INSTANCE.getSelectedValue("name");
            |}"""

        assertEquals(expectedFunSpecString.trimMargin(), funSpec.toString().trimMargin())
    }
}