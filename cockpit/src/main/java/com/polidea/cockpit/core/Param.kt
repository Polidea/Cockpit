package com.polidea.cockpit.core

interface Param<T : Any> {
    val name: String
    var value: T
    val description: String?
    val group: String?
}