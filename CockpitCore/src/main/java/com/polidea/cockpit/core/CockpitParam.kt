package com.polidea.cockpit.core

// TODO: extract to core library
data class CockpitParam<T : Any>(val name: String,
                                 var value: T,
                                 val description: String? = null,
                                 val group: String? = null)