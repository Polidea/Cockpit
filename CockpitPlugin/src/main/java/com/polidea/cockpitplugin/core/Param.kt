package com.polidea.cockpitplugin.core

// TODO: extract to core library
interface Param<T : Any> {
    val name: String
    var value: T
    val description: String?
    val group: String?
}