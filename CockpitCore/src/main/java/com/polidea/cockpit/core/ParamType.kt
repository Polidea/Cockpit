package com.polidea.cockpit.core

enum class ParamType(val value: String) {
    ACTION("action"),
    DEFAULT("");

    companion object {
        fun forValue(value: String?): ParamType {
            return values().find { it.value == value } ?: DEFAULT
        }
    }
}