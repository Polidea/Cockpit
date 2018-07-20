package com.polidea.cockpit.utils

import com.polidea.cockpit.core.CockpitParam

internal inline fun <reified T : Any> CockpitParam<Any>.isTypeOf(): Boolean =
        this.value is T

internal inline fun <reified T : Any> Collection<CockpitParam<Any>>.getParam(name: String): T =
        (this.find { it.name == name }
                ?: throw IllegalArgumentException("Param with name $name undefined!"))
                .let { it as T }

internal inline fun <reified T : Any> List<CockpitParam<Any>>.getParam(position: Int): T =
        this[position].let { it as T }

internal fun List<CockpitParam<Any>>.copy(): List<CockpitParam<Any>> {
    val paramsCopy = mutableListOf<CockpitParam<Any>>()
    this.forEach { paramsCopy.add(it.copy()) }
    return paramsCopy.toList()
}

internal fun List<CockpitParam<Any>>.toGroupedParams(): Map<String?, List<CockpitParam<Any>>> {
    val mutableGroupedParams = LinkedHashMap<String?, MutableList<CockpitParam<Any>>>()

    forEach {
        val params = mutableGroupedParams[it.group] ?: mutableListOf()
        params.add(it)
        mutableGroupedParams[it.group] = params
    }

    return mutableGroupedParams
}