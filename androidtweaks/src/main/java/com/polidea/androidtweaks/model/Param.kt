package com.polidea.androidtweaks.model


interface Param<T> {
    val name: String
    var value: T
}