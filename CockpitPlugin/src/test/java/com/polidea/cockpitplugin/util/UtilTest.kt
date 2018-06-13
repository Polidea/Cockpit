package com.polidea.cockpitplugin.util

import org.junit.Test
import kotlin.test.assertEquals

class UtilTest {

    @Test
    fun createUtilOneSetTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", false),
                Pair("p3", 0)
        ).toMutableMap()

        val mergedMap = Util.deepMerge(mainMap)
        val expectedMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", false),
                Pair("p3", 0)
        )
        assertEquals(expectedMap, mergedMap)
    }

    @Test
    fun createUtilEmptySetTest() {

        val mainMap = mapOf<String, Any>().toMutableMap()

        val mergedMap = Util.deepMerge(mainMap)
        val expectedMap = mapOf<String, Any>()
        assertEquals(expectedMap, mergedMap)
    }

    @Test
    fun createUtilDifferentTypesTwoSetsTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", false),
                Pair("p3", 0)
        ).toMutableMap()

        val buildTypeMap = mapOf(
                Pair("p2", true),
                Pair("p3", "debug"),
                Pair("p4", "debug"),
                Pair("p5", 1)
        )

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", true),
                Pair("p3", "debug"),
                Pair("p4", "debug"),
                Pair("p5", 1)
        )

        assertEquals(expectedMap, mergedMap)
    }

    @Test
    fun createUtilFullIntersectionTwoSetsTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", "main"),
                Pair("p3", "main"),
                Pair("p4", false),
                Pair("p5", 0)
        ).toMutableMap()

        val buildTypeMap = mapOf(
                Pair("p1", "debug"),
                Pair("p2", "debug"),
                Pair("p3", "debug"),
                Pair("p4", true),
                Pair("p5", 1)
        )

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "debug"),
                Pair("p2", "debug"),
                Pair("p3", "debug"),
                Pair("p4", true),
                Pair("p5", 1)
        )

        assertEquals(expectedMap, mergedMap)
    }

    @Test
    fun createUtilEmptyOverrideTwoSetsTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", "main"),
                Pair("p3", "main"),
                Pair("p4", false),
                Pair("p5", 0)
        ).toMutableMap()

        val buildTypeMap = mapOf<String, Any>()

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", "main"),
                Pair("p3", "main"),
                Pair("p4", false),
                Pair("p5", 0)
        )

        assertEquals(expectedMap, mergedMap)
    }


    @Test
    fun createUtilSameTypesThreeSetsTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", "main"),
                Pair("p3", "main")
        ).toMutableMap()

        val buildTypeMap = mapOf(
                Pair("p2", "debug"),
                Pair("p3", "debug"),
                Pair("p4", "debug"),
                Pair("p5", "debug")
        )

        val variantTypeMap = mapOf(
                Pair("p1", "flavorDebug"),
                Pair("p5", "flavorDebug"),
                Pair("p6", "flavorDebug")
        )

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap, variantTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "flavorDebug"),
                Pair("p2", "debug"),
                Pair("p3", "debug"),
                Pair("p4", "debug"),
                Pair("p5", "flavorDebug"),
                Pair("p6", "flavorDebug")
        )

        assertEquals(expectedMap, mergedMap)
    }


    @Test
    fun createUtilDifferentMergedTypesThreeSetsTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", false),
                Pair("p3", 0)
        ).toMutableMap()

        val buildTypeMap = mapOf(
                Pair("p2", true),
                Pair("p3", "debug"),
                Pair("p4", "debug"),
                Pair("p5", 1)
                )

        val variantTypeMap = mapOf(
                Pair("p1", "flavorDebug"),
                Pair("p5", 2),
                Pair("p6", "flavorDebug")
        )

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap, variantTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "flavorDebug"),
                Pair("p2", true),
                Pair("p3", "debug"),
                Pair("p4", "debug"),
                Pair("p5", 2),
                Pair("p6", "flavorDebug")
        )

        assertEquals(expectedMap, mergedMap)
    }

    @Test
    fun createUtilNoIntersectionThreeSetsTest() {

        val mainMap = mapOf<String, Any>(
                Pair("p1", "main"),
                Pair("p2", "main")
        ).toMutableMap()

        val buildTypeMap = mapOf(
                Pair("p3", 0),
                Pair("p4", "debug")
        )

        val variantTypeMap = mapOf(
                Pair("p5", true),
                Pair("p6", "flavorDebug")
        )

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap, variantTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", "main"),
                Pair("p3", 0),
                Pair("p4", "debug"),
                Pair("p5", true),
                Pair("p6", "flavorDebug")
        )

        assertEquals(expectedMap, mergedMap)
    }

    @Test
    fun createUtilFullIntersectionThreeSetsTest() {

        val mainMap = mapOf(
                Pair("p1", "main"),
                Pair("p2", "main"),
                Pair("p3", "main"),
                Pair("p4", false),
                Pair("p5", 0)
        ).toMutableMap()

        val buildTypeMap = mapOf(
                Pair("p1", "debug"),
                Pair("p2", "debug"),
                Pair("p3", "debug"),
                Pair("p4", false),
                Pair("p5", 1)
        )

        val variantTypeMap = mapOf(
                Pair("p1", "flavorDebug"),
                Pair("p2", "flavorDebug"),
                Pair("p3", "flavorDebug"),
                Pair("p4", true),
                Pair("p5", 2)
        )

        val mergedMap = Util.deepMerge(mainMap, buildTypeMap, variantTypeMap)
        val expectedMap = mapOf(
                Pair("p1", "flavorDebug"),
                Pair("p2", "flavorDebug"),
                Pair("p3", "flavorDebug"),
                Pair("p4", true),
                Pair("p5", 2)
        )

        assertEquals(expectedMap, mergedMap)
    }
}