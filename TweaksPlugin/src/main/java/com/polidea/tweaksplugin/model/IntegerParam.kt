package com.polidea.tweaksplugin.model


data class IntegerParam(override var name: String, override var value: Int) : Param<Int>