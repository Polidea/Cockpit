package com.polidea.cockpitplugin.util

import com.polidea.cockpitplugin.core.YamlParam
import com.polidea.cockpitplugin.model.*

class ParameterParser {

    fun parseValueMap(values: Map<String, YamlParam<*>>): ArrayList<Param<*>> {
        val paramList = ArrayList<Param<*>>()
        values.forEach { param: Map.Entry<String, YamlParam<*>> ->
            val key = param.key
            val extendedParam = param.value

            val value = extendedParam.value
            val description = extendedParam.description
            val group = extendedParam.group
            when (value) {
                is String -> paramList.add(StringParam(key, value, description, group))
                is Int -> paramList.add(IntegerParam(key, value, description, group))
                is Double -> paramList.add(DoubleParam(key, value, description, group))
                is Boolean -> paramList.add(BooleanParam(key, value, description, group))
                else -> throw IllegalArgumentException("CockpitTask: Param type undefined: $param!")
            }
        }
        return paramList
    }
}