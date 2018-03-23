package com.polidea.cockpitplugin.model


data class IntegerParam(override var name: String, override var value: Int) : Param<Int>