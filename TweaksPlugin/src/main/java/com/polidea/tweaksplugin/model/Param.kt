package com.polidea.tweaksplugin.model


interface Param<T: Any> {
    val name: String
    var value: T
}