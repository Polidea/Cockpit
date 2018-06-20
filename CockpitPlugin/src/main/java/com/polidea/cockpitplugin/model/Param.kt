package com.polidea.cockpitplugin.model


interface Param<T : Any> {
    val name: String
    var value: T
}