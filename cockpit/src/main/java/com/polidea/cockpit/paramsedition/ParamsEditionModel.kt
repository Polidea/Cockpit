package com.polidea.cockpit.paramsedition

import android.util.Log
import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.core.type.CockpitReadOnly
import com.polidea.cockpit.extensions.*
import com.polidea.cockpit.manager.CockpitManager

internal class ParamsEditionModel : ParamsModel {

    private lateinit var paramsCopy: List<CockpitParam<Any>>
    private lateinit var groupedParamsCopy: Map<String?, CockpitParamGroup>
    private lateinit var topLevelGroups: Map<String?, CockpitParamGroup>

    override lateinit var groupNames: List<String?>
        private set

    override val displaySize: Int
        get() = groupedParamsCopy.getDisplaySize()

    override val paramsSize: Int
        get() = paramsCopy.size

    override val groupsSize: Int
        get() = topLevelGroups.size

    init {
        setParams(CockpitManager.getParamsCopy())
    }

    override fun getGroupSize(groupIndex: Int) =
            groupedParamsCopy[getGroupName(groupIndex)]?.run { GroupSize(subgroups.size, params.size) }
                    ?: throw IllegalArgumentException("Couldn't find group for index: $groupIndex")

    override fun getGroupName(groupIndex: Int) = groupNames[groupIndex]

    override fun getSubgroupName(groupIndex: Int, subgroupIndex: Int): String? = topLevelGroups.values.toList()[groupIndex].subgroups[subgroupIndex].displayName

    override fun <T : Any> getParamAt(itemPosition: ItemPosition): CockpitParam<T> =
            groupedParamsCopy[getGroupName(itemPosition.groupIndex)]?.params?.getParam(itemPosition.paramIndex)
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

        setParams(restoredParams)
    }

    fun save() {
        CockpitManager.setParamValues(paramsCopy)
        CockpitManager.save()
    }

    private fun setParams(newParams: List<CockpitParam<Any>>) {
        paramsCopy = newParams
        groupedParamsCopy = paramsCopy.toGroupedParams().convertToGroups()
        topLevelGroups = groupedParamsCopy.topLevelGroups()
        groupNames = topLevelGroups.keys.toList()
    }
}