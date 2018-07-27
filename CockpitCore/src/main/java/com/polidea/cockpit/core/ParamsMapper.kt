package com.polidea.cockpit.core

import com.polidea.cockpit.core.exception.CockpitParseException
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitListType

// TODO [PU] It would be nice to see this class tested
class ParamsMapper {

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

    private fun fromSimpleYamlFormat(paramName: String, value: Any): CockpitParam<Any> {
        System.out.println(paramName + " " + value::class.java)
        val paramValue = when (value) {
            is List<*> -> CockpitListType(ArrayList<Any>(value), 0)
            else -> value
        }
        return CockpitParam(paramName, paramValue)
    }

    private fun fromExtendedYamlFormat(paramName: String, valueMap: Map<*, *>): CockpitParam<Any> {
        val type = ParamType.forValue(valueMap[KEY_TYPE] as String?)
        val value = when (type) {
            ParamsMapper.ParamType.ACTION -> CockpitAction(valueMap[KEY_ACTION_BUTTON_TEXT] as? String)
            ParamsMapper.ParamType.LIST -> {
                val values = valueMap[KEY_LIST_VALUES] as? List<*>
                        ?: throw CockpitParseException("$paramName parameter must contain list of elements in `$KEY_LIST_VALUES` field")
                val selectedIndex = (valueMap[KEY_LIST_SELECTION_INDEX] as Int?) ?: 0
                CockpitListType(ArrayList<Any>(values), selectedIndex)
            }
            ParamsMapper.ParamType.DEFAULT -> valueMap[KEY_VALUE] as Any
        }
        val description = valueMap[KEY_DESCRIPTION] as String?
        val group = valueMap[KEY_GROUP] as String?
        return CockpitParam(paramName, value, description, group)
    }

    fun toYamlMap(params: List<CockpitParam<Any>>): Map<String, Any> {
        return linkedMapOf(*params.map {
            if (it.description == null && it.group == null) { // simple parameter with value only
                val value = it.value
                when (value) {
                    is CockpitListType<*> -> toExtendedYamlFormat(it)
                    else -> toSimpleYamlFormat(it)
                }
            } else { // parameter with description or group (or both of them)
                toExtendedYamlFormat(it)
            }
        }.toTypedArray())
    }

    private fun toSimpleYamlFormat(it: CockpitParam<Any>) = Pair(it.name, it.value)

    private fun toExtendedYamlFormat(it: CockpitParam<Any>): Pair<String, LinkedHashMap<String, Any>> {
        val map = linkedMapOf<String, Any>()
        val value = it.value
        when (value) {
            is CockpitAction -> {
                map[KEY_TYPE] = ParamType.ACTION.value
                value.buttonText?.let { map[KEY_ACTION_BUTTON_TEXT] = it }
            }
            is CockpitListType<*> -> {
                map[KEY_TYPE] = ParamType.LIST.value
                map[KEY_LIST_VALUES] = value.items
                map[KEY_LIST_SELECTION_INDEX] = value.selectedIndex
            }
            else -> {
                value.let { map[KEY_VALUE] = it }
            }
        }
        it.description?.let { map[KEY_DESCRIPTION] = it }
        it.group?.let { map[KEY_GROUP] = it }
        return Pair(it.name, map)
    }


    companion object {
        private const val KEY_TYPE = "type"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_VALUE = "value"
        private const val KEY_GROUP = "group"

        private const val KEY_ACTION_BUTTON_TEXT = "buttonText"

        private const val KEY_LIST_VALUES = "values"
        private const val KEY_LIST_SELECTION_INDEX = "selectedItemIndex"
    }

    private enum class ParamType(val value: String) {
        ACTION("action"),
        LIST("list"),
        DEFAULT("");

        companion object {
            fun forValue(value: String?): ParamType = values().find { it.value == value } ?: DEFAULT
        }
    }
}