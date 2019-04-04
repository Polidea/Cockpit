package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup

internal class DisplayModel(val items: List<DisplayItem>)

internal sealed class DisplayItem(val displayName: String?) {
    class Section(displayName: String?) : DisplayItem(displayName)
    class Group(displayName: String?, val group: CockpitParamGroup) : DisplayItem(displayName)
    class Param(displayName: String?, val param: CockpitParam<Any>) : DisplayItem(displayName)
}

internal fun Map<String?, CockpitParamGroup>.toDisplayModel(): DisplayModel {
    val items = mutableListOf<DisplayItem>()
    forEach {
        items.addAll(it.value.toDisplayModel().items)
    }

    return DisplayModel(items)
}

internal fun CockpitParamGroup.toDisplayModel(): DisplayModel {
    val items = mutableListOf<DisplayItem>(DisplayItem.Section(displayName))
    items.addAll(subgroups.map { DisplayItem.Group(it.displayName, it) })
    items.addAll(params.map { DisplayItem.Param(it.description ?: it.name, it) })

    return DisplayModel(items)
}
