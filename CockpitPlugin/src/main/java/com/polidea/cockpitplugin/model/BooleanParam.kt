package com.polidea.cockpitplugin.model


data class BooleanParam(override var name: String, override var value: Boolean,
                        override val description: String?, override val group: String?) : Param<Boolean>