package com.polidea.cockpit.core.type

class CockpitColor(val value: String) {

    val color: Int

    init {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Unknown color")
        }
        // from android.graphics.Color.parseColor(colorString: String)
        if (value[0] == '#') {
            // Use a long to avoid rollovers on #ffXXXXXX
            var color = java.lang.Long.parseLong(value.substring(1), 16)
            if (value.length == 7) {
                // Set the alpha value
                color = color or -0x1000000
            } else if (value.length != 9) {
                throw IllegalArgumentException("Unknown color")
            }
            this.color = color.toInt()
        } else {
            throw IllegalArgumentException("Unknown color")
        }
    }
}