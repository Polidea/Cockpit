package com.polidea.cockpit.utils

import com.polidea.cockpit.core.CockpitParam

inline fun <reified T : Any> CockpitParam<Any>.isTypeOf(): Boolean =
        this.value is T

inline fun <reified T : Any> Collection<CockpitParam<Any>>.getParam(name: String): T =
        (this.find { it.name == name }
                ?: throw IllegalArgumentException("Param with name $name undefined!"))
                .let { it as T }

inline fun <reified T : Any> List<CockpitParam<Any>>.getParam(position: Int): T =
        this[position].let { it as T }

fun List<CockpitParam<Any>>.copy(): List<CockpitParam<Any>> {
    val paramsCopy = mutableListOf<CockpitParam<Any>>()
    this.forEach { paramsCopy.add(it.copy()) }
    return paramsCopy.toList()
}
