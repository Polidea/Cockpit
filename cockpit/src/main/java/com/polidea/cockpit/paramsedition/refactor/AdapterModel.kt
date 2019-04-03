package com.polidea.cockpit.paramsedition.refactor

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup

internal class DisplayModel(val items: List<DisplayItem>)

internal sealed class DisplayItem(val displayName: String?) {
    class Section(displayName: String?) : DisplayItem(displayName)
    class Group(displayName: String?, val group: CockpitParamGroup) : DisplayItem(displayName)
    class Param(displayName: String?, val param: CockpitParam<Any>) : DisplayItem(displayName)
//    class TopNavigation : DisplayItem(null)
//    class BackNavigation : DisplayItem(null)
}

internal fun Map<String?, CockpitParamGroup>.toDisplayModel(): DisplayModel {
    val items = mutableListOf<DisplayItem>()
    forEach {
        items.addAll(it.value.toDisplayModel().items)
    }

    return DisplayModel(items)
}

internal fun CockpitParamGroup.toDisplayModel(addTopNavigation: Boolean = false): DisplayModel {
    val items = mutableListOf<DisplayItem>(DisplayItem.Section(displayName))
//    if (addTopNavigation) {
//        items.add(DisplayItem.TopNavigation())
//        items.add(DisplayItem.BackNavigation())
//    }
    items.addAll(subgroups.map { DisplayItem.Group(it.displayName, it) })
    items.addAll(params.map { DisplayItem.Param(it.description ?: it.name, it) })

    return DisplayModel(items)
}
