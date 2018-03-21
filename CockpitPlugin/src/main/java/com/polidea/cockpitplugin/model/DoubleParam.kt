package com.polidea.cockpitplugin.model


data class DoubleParam(override var name: String, override var value: Double) : Param<Double>