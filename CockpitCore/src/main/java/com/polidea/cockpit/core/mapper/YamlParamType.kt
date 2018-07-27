package com.polidea.cockpit.core.mapper

internal enum class YamlParamType(val value: String) {
    ACTION("action"),
    LIST("list"),
    COLOR("color"),
    DEFAULT("");

    companion object {
        fun forValue(value: String?): YamlParamType = values().find { it.value == value } ?: DEFAULT
    }
}