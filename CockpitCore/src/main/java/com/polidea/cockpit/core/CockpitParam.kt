package com.polidea.cockpit.core

data class CockpitParam<T : Any>(val name: String,
                                 var value: T,
                                 val type: ParamType = ParamType.DEFAULT,
                                 val description: String? = null,
                                 val group: String? = null)