package com.polidea.cockpitplugin.model


data class StringParam(override var name: String, override var value: String,
                       override val description: String?, override val group: String?) : Param<String>