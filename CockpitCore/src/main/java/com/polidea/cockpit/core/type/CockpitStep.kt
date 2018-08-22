package com.polidea.cockpit.core.type

data class CockpitStep<T : Number>(val min: T?, val max: T?, val step: T, val value: T) {

    init {
        if (min != null && max != null && min.toDouble() > max.toDouble())
            throw IllegalArgumentException("Max value must be greater than min value")
        if (min != null && value.toDouble() < min.toDouble())
            throw IllegalArgumentException("Selected value must be greater than min value")
        if (max != null && value.toDouble() > max.toDouble())
            throw IllegalArgumentException("Selected value must be less than max value")
        if (step.toDouble() <= 0)
            throw IllegalArgumentException("Step value must be positive")
    }
}