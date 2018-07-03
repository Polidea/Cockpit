package com.polidea.cockpit.manager

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.event.PropertyChangeListener
import com.polidea.cockpit.utils.FileUtils
import com.polidea.cockpit.utils.copy
import com.polidea.cockpit.utils.getParam

object CockpitManager {

    private val params: MutableList<CockpitParam<Any>> by lazy {
        FileUtils.getParams().toMutableList()
    }

    val defaultParams: List<CockpitParam<Any>> by lazy {
        FileUtils.getDefaultParams()
    }

    private val paramChangeNotifier = ParamChangeNotifier()

    fun addParam(param: CockpitParam<*>) {
        checkIfExistsAndAddParam(param)
    }

    private fun checkIfExistsAndAddParam(param: CockpitParam<*>) {
        if (!exists(param.name)) {
            System.out.println("Param ${param.name} doesn't exist, adding")
            params.add(CockpitParam(param.name, param.value, param.description, param.group))
        } else {
            System.out.println("Param ${param.name} already exists")
        }
    }

    private fun exists(key: String) =
            params.find { it.name == key } != null

    fun <T : Any> getParamValue(name: String): T = params.getParam<CockpitParam<T>>(name).value

    fun <T : Any> getParamDefaultValue(name: String): T = defaultParams.getParam<CockpitParam<T>>(name).value

    fun setParamsValue(params: Collection<CockpitParam<Any>>) {
        params.forEach { setParamValueWithAutoSave(it.name, it.value, false) }
        save()
    }

    fun <T : Any> setParamValue(name: String, newValue: T) {
        setParamValueWithAutoSave(name, newValue, true)
    }

    private fun <T : Any> setParamValueWithAutoSave(name: String, newValue: T, autoSave: Boolean) {
        val param = params.getParam<CockpitParam<T>>(name)
        val oldValue = param.value
        param.value = newValue
        paramChangeNotifier.firePropertyChange(name, oldValue, newValue)
        if (autoSave)
            save()
    }

    fun <T : Any> addOnParamChangeListener(name: String, listener: PropertyChangeListener<T>) {
        paramChangeNotifier.add(name, listener)
    }

    fun <T : Any> removeOnParamChangeListener(name: String, listener: PropertyChangeListener<T>) {
        paramChangeNotifier.remove(name, listener)
    }

    fun clear() {
        params.clear()
    }

    fun getParamsCopy(): List<CockpitParam<Any>> = params.copy()

    fun getDefaultParamsCopy(): List<CockpitParam<Any>> = defaultParams.copy()

    private fun save() {
        FileUtils.saveCockpitAsYaml(params)
    }
}
