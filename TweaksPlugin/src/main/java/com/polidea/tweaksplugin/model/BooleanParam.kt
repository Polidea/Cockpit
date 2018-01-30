package com.polidea.tweaksplugin.model


data class BooleanParam(override var name: String, override var value: Boolean) : Param<Boolean>