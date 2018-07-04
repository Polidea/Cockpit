package com.polidea.cockpit.manager

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.event.PropertyChangeListener
import com.polidea.cockpit.utils.FileUtils
import com.polidea.cockpit.utils.copy
import com.polidea.cockpit.utils.getParam
import org.jetbrains.annotations.TestOnly

object CockpitManager {

    private val params: MutableList<CockpitParam<Any>> by lazy {
        FileUtils.getParams().toMutableList()
    }

    private val defaultParams: List<CockpitParam<Any>> by lazy {
        FileUtils.getDefaultParams()
    }

    private val paramChangeNotifier = ParamChangeNotifier()

    internal fun addParam(param: CockpitParam<*>) {
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

    internal fun setParamValues(params: Collection<CockpitParam<Any>>) {
        params.forEach { setParamValue(it.name, it.value) }
    }

    fun <T : Any> setParamValue(name: String, newValue: T) {
        val param = params.getParam<CockpitParam<T>>(name)
        val oldValue = param.value
        param.value = newValue
        paramChangeNotifier.firePropertyChange(name, oldValue, newValue)
    }

    fun <T : Any> addOnParamChangeListener(name: String, listener: PropertyChangeListener<T>) {
        paramChangeNotifier.add(name, listener)
    }

    fun <T : Any> removeOnParamChangeListener(name: String, listener: PropertyChangeListener<T>) {
        paramChangeNotifier.remove(name, listener)
    }

    internal fun getParamsCopy(): List<CockpitParam<Any>> = params.copy()

    internal fun getDefaultParamsCopy(): List<CockpitParam<Any>> = defaultParams.copy()

    fun save() {
        FileUtils.saveCockpitAsYaml(params)
    }

    @TestOnly
    internal fun clear() {
        params.clear()
    }
}
