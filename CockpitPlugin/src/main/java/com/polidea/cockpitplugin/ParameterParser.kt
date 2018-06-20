package com.polidea.cockpitplugin

import com.polidea.cockpitplugin.model.*

class ParameterParser {

    fun parseValueMap(values: Map<String, Any>): ArrayList<Param<*>> {
        val paramList = ArrayList<Param<*>>()

        values.map {
            val key = it.key
            val value = it.value
            when (value) {
                is String -> paramList.add(StringParam(key, value))
                is Int -> paramList.add(IntegerParam(key, value))
                is Double -> paramList.add(DoubleParam(key, value))
                is Boolean -> paramList.add(BooleanParam(key, value))
                else -> throw IllegalArgumentException("Param type undefined: $it!")
            }
        }

        return paramList
    }
}