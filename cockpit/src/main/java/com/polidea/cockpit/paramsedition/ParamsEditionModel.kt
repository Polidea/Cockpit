package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.manager.CockpitManager
import com.polidea.cockpit.utils.getParam
import com.polidea.cockpit.utils.toGroupedParams

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
                    ?: throw IllegalArgumentException("Cannot find param for $itemPosition.groupIndex group index and $itemPosition.paramIndex param index")

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
        paramsCopy = CockpitManager.getDefaultParamsCopy()
        groupedParamsCopy = paramsCopy.toGroupedParams()
    }

    fun save() {
        CockpitManager.setParamValues(paramsCopy)
        CockpitManager.save()
    }
}