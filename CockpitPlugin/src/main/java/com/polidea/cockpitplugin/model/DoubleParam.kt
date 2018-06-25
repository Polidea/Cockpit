package com.polidea.cockpitplugin.model


data class DoubleParam(override var name: String, override var value: Double,
                       override val description: String?, override val group: String?) : Param<Double>