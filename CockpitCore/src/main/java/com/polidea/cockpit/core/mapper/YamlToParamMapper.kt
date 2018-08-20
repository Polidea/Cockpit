package com.polidea.cockpit.core.mapper

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.exception.CockpitParseException
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType
import com.polidea.cockpit.core.type.CockpitRange

internal class YamlToParamMapper {

    fun toListOfParams(yamlMap: Map<String, Any>): List<CockpitParam<Any>> {
        return yamlMap.map {
            val value = it.value
            val param = when (value) {
                is Map<*, *> -> fromExtendedYamlFormat(it.key, value) // new extended yaml format
                else -> fromSimpleYamlFormat(it.key, value)           // previous simple yaml format
            }
            param
        }
    }

    private fun fromSimpleYamlFormat(paramName: String, paramValue: Any): CockpitParam<Any> {
        val paramVal = when (paramValue) {
            is List<*> -> CockpitListType(ArrayList<Any>(paramValue), 0)
            else -> paramValue
        }
        return CockpitParam(paramName, paramVal)
    }

    private fun fromExtendedYamlFormat(paramName: String, valueMap: Map<*, *>): CockpitParam<Any> {
        val type = YamlParamType.forValue(valueMap[MapperConsts.KEY_TYPE] as String?)
        val value = when (type) {
            YamlParamType.ACTION -> createCockpitAction(valueMap)
            YamlParamType.LIST -> createCockpitListType(paramName, valueMap)
            YamlParamType.COLOR -> createCockpitColor(paramName, valueMap)
            YamlParamType.RANGE -> createCockpitRange(paramName, valueMap)
            YamlParamType.DEFAULT -> valueMap[MapperConsts.KEY_VALUE] as Any
        }
        val description = valueMap[MapperConsts.KEY_DESCRIPTION] as String?
        val group = valueMap[MapperConsts.KEY_GROUP] as String?
        return CockpitParam(paramName, value, description, group)
    }

    private fun createCockpitAction(valueMap: Map<*, *>) =
            CockpitAction(valueMap[MapperConsts.KEY_ACTION_BUTTON_TEXT] as? String)

    private fun createCockpitListType(paramName: String, valueMap: Map<*, *>): CockpitListType<Any> {
        val values = (valueMap[MapperConsts.KEY_LIST_VALUES] as? List<*>
                ?: throw CockpitParseException("$paramName parameter must contain list of elements in `${MapperConsts.KEY_LIST_VALUES}` field")).filterNotNull()

        val types = values.distinctBy { it::class.java }.count()
        if (types > 1) {
            throw CockpitParseException("In $paramName: list with mixed types is not supported!")
        }

        val selectedIndex = (valueMap[MapperConsts.KEY_LIST_SELECTION_INDEX] as Int?) ?: 0
        return CockpitListType(ArrayList<Any>(values), selectedIndex)
    }

    private fun createCockpitColor(paramName: String, valueMap: Map<*, *>): CockpitColor {
        val colorValue = valueMap[MapperConsts.KEY_VALUE] as? String
                ?: throw CockpitParseException("$paramName must contain String value param")
        try {
            return CockpitColor(colorValue)
        } catch (e: IllegalArgumentException) {
            throw CockpitParseException("Invalid color format for `$paramName` param. Supported formats are: #RRGGBB and #AARRGGBB.")
        }
    }

    private fun createCockpitRange(paramName: String, valueMap: Map<*, *>): CockpitRange<*> {
        val min = valueMap[MapperConsts.KEY_RANGE_MIN] as? Number
                ?: throw CockpitParseException("$paramName parameter must contain min and max fields")
        val max = valueMap[MapperConsts.KEY_RANGE_MAX] as? Number
                ?: throw throw CockpitParseException("$paramName parameter must contain min and max fields")
        val step = valueMap[MapperConsts.KEY_RANGE_STEP] as? Number ?: 1
        val selectedValue = valueMap[MapperConsts.KEY_RANGE_VALUE] as? Number ?: min

        try {
            if (min is Int && max is Int && step is Int && selectedValue is Int)
                return CockpitRange(min.toInt(), max.toInt(), step.toInt(), selectedValue.toInt())

            return CockpitRange(min.toDouble(), max.toDouble(), step.toDouble(), selectedValue.toDouble())
        } catch (e: IllegalArgumentException) {
            throw CockpitParseException("$paramName`s ${e.message}")
        }
    }
}