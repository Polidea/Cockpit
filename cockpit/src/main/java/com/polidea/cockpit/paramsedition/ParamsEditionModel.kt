package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitReadOnly
import com.polidea.cockpit.extensions.getParam
import com.polidea.cockpit.extensions.toGroupedParams
import com.polidea.cockpit.manager.CockpitManager

internal class ParamsEditionModel : ParamsModel {

    private var paramsCopy: List<CockpitParam<Any>> = CockpitManager.getParamsCopy()

    private var groupedParamsCopy: Map<String?, List<CockpitParam<Any>>> = paramsCopy.toGroupedParams()

    override val paramsSize: Int
        get() = paramsCopy.size

    override val groupsSize: Int
        get() = groupedParamsCopy.size

    override fun getGroupSize(groupIndex: Int) =
            groupedParamsCopy[getGroupName(groupIndex)]?.size
                    ?: throw IllegalArgumentException("Couldn't find group for index: $groupIndex")

    override fun getGroupName(groupIndex: Int) = groupedParamsCopy.keys.toList()[groupIndex]

    override fun <T : Any> getParamAt(itemPosition: ItemPosition): CockpitParam<T> =
            groupedParamsCopy[getGroupName(itemPosition.groupIndex)]?.getParam(itemPosition.paramIndex)
                    ?: throw IllegalArgumentException("Cannot find param for ${itemPosition.groupIndex} group index and ${itemPosition.paramIndex} param index")

    fun <T : Any> setValue(itemPosition: ItemPosition, newValue: T) {
        val param = getParamAt<T>(itemPosition)
        param.value = newValue
        CockpitManager.setParamValue(param.name, newValue)
    }

    fun <T : Any> selectValue(itemPosition: ItemPosition, selectedItemIndex: Int) {
        val param = getParamAt<T>(itemPosition)
        CockpitManager.selectParamValue<T>(param.name, selectedItemIndex)
    }

    fun restoreValue(itemPosition: ItemPosition) {
        val param = getParamAt<CockpitParam<Any>>(itemPosition)
        param.value = CockpitManager.getParamDefaultValue(param.name)
    }

    fun requestAction(itemPosition: ItemPosition) {
        val param = getParamAt<CockpitParam<Any>>(itemPosition)
        CockpitManager.requestAction(param.name)
    }

    fun restoreAll() {
        val restoredParams = CockpitManager.getDefaultParamsCopy()
        restoredParams.forEachIndexed { index, param ->
            if (param.value is CockpitReadOnly) {
                //Keeping current value prevents CockpitReadOnly param from being emptied.
                param.value = paramsCopy[index].value
            }
        }

        paramsCopy = restoredParams
        groupedParamsCopy = paramsCopy.toGroupedParams()
    }

    fun save() {
        CockpitManager.setParamValues(paramsCopy)
        CockpitManager.save()
    }
}