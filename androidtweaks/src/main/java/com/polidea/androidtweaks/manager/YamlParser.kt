package com.polidea.androidtweaks.manager

import android.content.Context
import com.polidea.androidtweaks.model.*
import org.yaml.snakeyaml.Yaml


class YamlParser {

    fun parseYaml(fileName: String, context: Context): List<Param<*>> {
        val yaml = Yaml()
        val values: Map<String, Any> = yaml.load(context.assets.open(fileName).bufferedReader().use {
            it.readText()
        })
        val paramList = ArrayList<Param<*>>()

        for (v in values) {
            when (v.value) {
                is String -> paramList.add(StringParam(v.key, v.value as String))
                is Int -> paramList.add(IntegerParam(v.key, v.value as Int))
                is Double -> paramList.add(DoubleParam(v.key, v.value as Double))
                is Boolean -> paramList.add(BooleanParam(v.key, v.value as Boolean))
            }
        }

        return paramList
    }
}