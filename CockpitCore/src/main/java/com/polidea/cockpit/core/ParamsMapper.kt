package com.polidea.cockpit.core

import com.polidea.cockpit.core.type.CockpitAction

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

    private fun fromSimpleYamlFormat(paramName: String, value: Any) = CockpitParam(paramName, value)

    private fun fromExtendedYamlFormat(paramName: String, valueMap: Map<*, *>): CockpitParam<Any> {
        val type = ParamType.forValue(valueMap[KEY_TYPE] as String?)
        val value = when (type) {
            ParamsMapper.ParamType.ACTION -> CockpitAction(valueMap[KEY_ACTION_BUTTON_TEXT] as? String)
            ParamsMapper.ParamType.DEFAULT -> valueMap[KEY_VALUE] as Any
        }
        val description = valueMap[KEY_DESCRIPTION] as String?
        val group = valueMap[KEY_GROUP] as String?
        return CockpitParam(paramName, value, description, group)
    }

    fun toYamlMap(params: List<CockpitParam<Any>>): Map<String, Any> {
        return linkedMapOf(*params.map {
            if (it.description == null && it.group == null) { // simple parameter with value only
                toSimpleYamlFormat(it)
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
    }

    private enum class ParamType(val value: String) {
        ACTION("action"),
        DEFAULT("");

        companion object {
            fun forValue(value: String?): ParamType = values().find { it.value == value } ?: DEFAULT
        }
    }
}