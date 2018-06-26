package com.polidea.cockpit.manager

import com.polidea.cockpit.event.PropertyChangeListener

object CockpitManager {

    val params: MutableList<CockpitParam<Any>> = ArrayList()

    fun addParam(param: CockpitParam<Any>) {
        checkIfExistsAndAddParam(param)
    }

    private fun checkIfExistsAndAddParam(param: CockpitParam<Any>) {
        if (!exists(param.name)) {
            System.out.println("Param ${param.name} doesn't exist, adding")
            params.add(param)
        } else {
            System.out.println("Param ${param.name} already exists")
        }
    }

    fun <T> getParamValue(name: String): T? = getParam<CockpitParam<T>>(name).value

    fun <T> setParamValue(key: String, value: T) {
        val param = getParam<CockpitParam<T>>(key)
        param.value = value
    }

    fun <T> addOnParamChangeListener(key: String, listener: PropertyChangeListener<T>) {
        val param = getParam<CockpitParam<T>>(key)
        param.addPropertyChangeListener(listener)
    }

    fun <T> removeOnParamChangeListener(key: String, listener: PropertyChangeListener<T>) {
        val param = getParam<CockpitParam<T>>(key)
        param.removePropertyChangeListener(listener)
    }

    fun exists(key: String) =
            params.find { it.name == key } != null

    fun clear() {
        params.clear()
    }

    private inline fun <reified T> getParam(name: String): T =
            (params.find { it.name == name }
                    ?: throw IllegalArgumentException("Param with name $name undefined!"))
                    .let { it as T }
}