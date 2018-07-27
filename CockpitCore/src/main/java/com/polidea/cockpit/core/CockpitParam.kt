package com.polidea.cockpit.core
// TODO [PU] It would be nice to have a documentation that would describe how to use main classes and what
// are the conditions regarding data format
data class CockpitParam<T : Any>(val name: String,
                                 var value: T,
                                 val description: String? = null,
                                 val group: String? = null)