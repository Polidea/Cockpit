package com.polidea.cockpit.core.type

class CockpitColor(val value: String) {

    init {
        if (!value.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})\$"))) {
            throw IllegalArgumentException("Unknown color")
        }
    }
}