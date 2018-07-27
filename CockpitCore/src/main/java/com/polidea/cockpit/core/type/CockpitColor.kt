package com.polidea.cockpit.core.type

import com.polidea.cockpit.core.ColorMatcher

class CockpitColor(val value: String) {

    init {
        if (!ColorMatcher.isHexColor(value)) {
            throw IllegalArgumentException("Unknown color")
        }
    }
}