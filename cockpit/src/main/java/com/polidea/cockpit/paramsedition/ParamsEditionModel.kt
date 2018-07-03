package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.utils.getParam

class ParamsEditionModel {

    private var paramsCopy: List<CockpitParam<Any>> = CockpitManager.getParamsCopy()

    val size : Int
        get() = paramsCopy.size

    fun <T : Any> getParamAt(position: Int): CockpitParam<T> =
            paramsCopy.getParam(position)

    fun <T : Any> setValue(position: Int, newValue: T) {
        paramsCopy[position].value = newValue
    }

    fun restoreValue(position: Int) {
        val param = paramsCopy[position]
        param.value = CockpitManager.getParamDefaultValue(param.name)
    }

    fun restoreAll() {
        paramsCopy = CockpitManager.getDefaultParamsCopy()
    }

    fun save() {
        CockpitManager.setParamsValue(paramsCopy)
    }
}