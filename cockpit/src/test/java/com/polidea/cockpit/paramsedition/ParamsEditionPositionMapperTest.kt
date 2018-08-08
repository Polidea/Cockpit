package com.polidea.cockpit.paramsedition

import android.content.Context
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.persistency.CockpitYamlFileManager
import com.polidea.cockpit.utils.FileUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ParamsEditionPositionMapperTest {

    private val context: Context = mockk(relaxed = true)
    private val cockpitYamlFileManager: CockpitYamlFileManager = spyk(CockpitYamlFileManager(DIRECTORY_PATH, context.assets))

    init {
        File(DIRECTORY_PATH).mkdirs()
        FileUtils.init(DIRECTORY_PATH, context.assets)
    }

    @Before
    fun setUp() {
        every { context.assets } returns mockk(relaxed = true)
        FileUtils.setCockpitYamlFileManager(cockpitYamlFileManager)

        every { cockpitYamlFileManager.readInputParams() } returns getTestCockpitParams()
        every { cockpitYamlFileManager.readSavedParams() } returns emptyList()
    }

    @Test
    fun toItemPositionAGroup() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(0)
        assertTrue(itemPosition.isGroupPosition())
        assertEquals(0, itemPosition.groupIndex)

        CockpitManager.clear()
    }

    @Test
    fun toItemPositionDoubleParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(1)
        assertFalse(itemPosition.isGroupPosition())
        assertEquals(0, itemPosition.groupIndex)
        assertEquals(0, itemPosition.paramIndex)

        CockpitManager.clear()
    }

    @Test
    fun toItemPositionBooleanParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(2)
        assertFalse(itemPosition.isGroupPosition())
        assertEquals(0, itemPosition.groupIndex)
        assertEquals(1, itemPosition.paramIndex)

        CockpitManager.clear()
    }

    @Test
    fun toItemPositionDefaultGroup() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(3)
        assertTrue(itemPosition.isGroupPosition())
        assertEquals(1, itemPosition.groupIndex)
    }

    @Test
    fun toItemPositionStringParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(4)
        assertFalse(itemPosition.isGroupPosition())
        assertEquals(1, itemPosition.groupIndex)
        assertEquals(0, itemPosition.paramIndex)

        CockpitManager.clear()
    }

    @Test
    fun toItemPositionBGroup() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(5)
        assertTrue(itemPosition.isGroupPosition())
        assertEquals(2, itemPosition.groupIndex)

        CockpitManager.clear()
    }

    @Test
    fun toItemPositionIntParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val itemPosition = mapper.toItemPosition(6)
        assertFalse(itemPosition.isGroupPosition())
        assertEquals(2, itemPosition.groupIndex)
        assertEquals(0, itemPosition.paramIndex)

        CockpitManager.clear()
    }

    @Test
    fun toAdapterPositionAGroup() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(0))
        assertEquals(0, adapterPosition)

        CockpitManager.clear()
    }

    @Test
    fun toAdapterPositionDoubleParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(0, 0))
        assertEquals(1, adapterPosition)

        CockpitManager.clear()
    }

    @Test
    fun toAdapterPositionBooleanParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(0, 1))
        assertEquals(2, adapterPosition)

        CockpitManager.clear()
    }

    @Test
    fun toAdapterPositionDefaultGroup() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(1))
        assertEquals(3, adapterPosition)

        CockpitManager.clear()
    }

    @Test
    fun toAdapterPositionStringParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(1, 0))
        assertEquals(4, adapterPosition)

        CockpitManager.clear()
    }

    @Test
    fun toAdapterPositionBGroup() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(2))
        assertEquals(5, adapterPosition)

        CockpitManager.clear()
    }


    @Test
    fun toAdapterPositionIntParam() {
        addParamsToCockpit(getTestCockpitParams())
        val mapper = createMapper()

        val adapterPosition = mapper.toAdapterPosition(ItemPosition(2, 0))
        assertEquals(6, adapterPosition)

        CockpitManager.clear()
    }

    private fun createMapper() = ParamsEditionPositionMapper(ParamsEditionModel())

    private fun addParamsToCockpit(params: List<CockpitParam<Any>>) {
        params.forEach { CockpitManager.addParam(it) }
    }

    private fun getTestCockpitParams(): List<CockpitParam<Any>> {
        val testParams: MutableList<CockpitParam<Any>> = mutableListOf()

        testParams.add(CockpitParam("doubleParam", 3.0, null, "a"))
        testParams.add(CockpitParam("booleanParam", false, null, "a"))
        testParams.add(CockpitParam("stringParam", "testValue"))
        testParams.add(CockpitParam("integerParam", 2, null, "b"))

        return testParams
    }

    companion object {
        private const val DIRECTORY_PATH = "data"
    }
}