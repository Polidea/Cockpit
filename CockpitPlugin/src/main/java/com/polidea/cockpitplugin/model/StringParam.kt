package com.polidea.cockpitplugin.model


data class StringParam(override var name: String, override var value: String) : Param<String>