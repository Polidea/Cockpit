package com.polidea.cockpit.core.type

data class CockpitRange<T : Number>(val min: T, val max: T, val step: T, val value: T) {

    init {
        if (min.toDouble() > max.toDouble())
            throw IllegalArgumentException("Max value must be greater than min value")
        if (step.toDouble() <= 0)
            throw IllegalArgumentException("Step value must be positive")
        if (value.toDouble() < min.toDouble() || value.toDouble() > max.toDouble())
            throw IllegalArgumentException("Selected value must be in the range")
    }
}