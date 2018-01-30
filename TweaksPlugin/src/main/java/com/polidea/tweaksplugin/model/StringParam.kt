package com.polidea.tweaksplugin.model


data class StringParam(override var name: String, override var value: String) : Param<String>