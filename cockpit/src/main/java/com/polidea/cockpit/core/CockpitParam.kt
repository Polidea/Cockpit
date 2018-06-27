package com.polidea.cockpit.core

// TODO: extract to core library
data class CockpitParam<T : Any>(override val name: String,
                            override var value: T,
                            override val description: String?,
                            override val group: String?): Param<T>