package com.polidea.tweaksplugin.model


interface Param<T> {
    val name: String
    var value: T
}