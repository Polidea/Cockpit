package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.core.type.CockpitReadOnly
import com.polidea.cockpit.extensions.*
import com.polidea.cockpit.manager.CockpitManager

internal class ParamsEditionModel : ParamsModel {

    private lateinit var paramsCopy: Map<String, CockpitParam<Any>>
    private lateinit var groupedParamsCopy: Map<String?, CockpitParamGroup>
    override lateinit var topLevelGroups: Map<String?, CockpitParamGroup>
        private set

    init {
        setParams(CockpitManager.getParamsCopy())
    }

    override fun <T : Any> getParam(paramName: String): CockpitParam<T>
            = paramsCopy[paramName] as CockpitParam<T>? ?: throw IllegalArgumentException("Cannot find param for name $paramName")

    override fun <T : Any> setValue(paramName: String, newValue: T) {
        getParam<T>(paramName).value = newValue
        CockpitManager.setParamValue(paramName, newValue)
    }

    override fun <T : Any> selectValue(paramName: String, selectedItemIndex: Int) {
        CockpitManager.selectParamValue<T>(paramName, selectedItemIndex)
    }

    override fun restoreValue(paramName: String) {
        getParam<CockpitParam<Any>>(paramName).value = CockpitManager.getParamDefaultValue(paramName)
    }

    override fun requestAction(paramName: String) {
        CockpitManager.requestAction(paramName)
    }

    override fun restoreAll() {
        val restoredParams = CockpitManager.getDefaultParamsCopy()
        restoredParams.forEach { param ->
            if (param.value is CockpitReadOnly) {
                //Keeping current value prevents CockpitReadOnly param from being emptied.
                paramsCopy[param.name]?.apply { param.value = value }
            }
        }

        setParams(restoredParams)
    }

    override fun save() {
        CockpitManager.setParamValues(paramsCopy.values)
        CockpitManager.save()
    }

    private fun setParams(newParams: List<CockpitParam<Any>>) {
        paramsCopy = newParams.map { it.name to it }.toMap()
        groupedParamsCopy = paramsCopy.values.toList().toGroupedParams().convertToGroups()
        topLevelGroups = groupedParamsCopy.topLevelGroups()
    }
}