package com.polidea.cockpit.manager

import com.polidea.cockpit.listener.CockpitParamChangeListener

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

    fun <T> getParamValue(name: String): T? = getParam<CockpitParam<T>>(name)?.value

    fun <T> setParamValue(key: String, value: T) {
        val param = getParam<CockpitParam<T>>(key)
        param ?: throw IllegalArgumentException("Param with name $key undefined!")
        param.value = value
    }

    fun <T> setOnParamChangeListener(key: String, listener: CockpitParamChangeListener<T>?) {
        val param = getParam<CockpitParam<T>>(key)
        param ?: throw IllegalArgumentException("Param with name $key undefined!")
        param.cockpitParamChangeListener = listener
    }

    fun exists(key: String) =
            params.find { it.name == key } != null

    fun clear() {
        params.clear()
    }

    private inline fun <reified T> getParam(name: String): T? =
            params.find { it.name == name }?.let { if (it !is T) null else it }
}