package com.polidea.cockpit.manager

import com.polidea.cockpit.core.Param
import com.polidea.cockpit.event.PropertyChangeListener
import kotlin.collections.ArrayList

object CockpitManager {

    val params: MutableList<Param<*>> = ArrayList()

    var defaultParams: MutableList<Param<*>> = mutableListOf()

    fun addParam(param: Param<*>) {
        checkIfExistsAndAddParam(param)
    }

    private fun checkIfExistsAndAddParam(param: Param<*>) {
        if (!exists(param.name)) {
            System.out.println("Param ${param.name} doesn't exist, adding")
            params.add(NotifiableParam(param.name, param.value, param.description, param.group))
        } else {
            System.out.println("Param ${param.name} already exists")
        }
    }

    fun <T : Any> getParamValue(name: String): T? = getParam<Param<T>>(name).value

    fun <T : Any> setParamValue(key: String, value: T) {
        val param = getParam<Param<T>>(key)
        param.value = value
    }

    fun <T : Any> addOnParamChangeListener(key: String, listener: PropertyChangeListener<T>) {
        val param = getParam<NotifiableParam<T>>(key)
        param.addPropertyChangeListener(listener)
    }

    fun <T : Any> removeOnParamChangeListener(key: String, listener: PropertyChangeListener<T>) {
        val param = getParam<NotifiableParam<T>>(key)
        param.removePropertyChangeListener(listener)
    }

    fun exists(key: String) =
            params.find { it.name == key } != null

    fun clear() {
        params.clear()
        defaultParams.clear()
    }

    private inline fun <reified T : Any> getParam(name: String): T =
            (params.find { it.name == name }
                    ?: throw IllegalArgumentException("Param with name $name undefined!"))
                    .let { it as T }
}