package com.polidea.cockpitplugin.model


data class IntegerParam(override var name: String, override var value: Int,
                        override val description: String?, override val group: String?) : Param<Int>