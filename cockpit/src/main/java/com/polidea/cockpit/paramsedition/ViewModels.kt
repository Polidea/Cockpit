package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup

internal class DisplayModel(val items: List<DisplayItem>, val breadcrumb: Breadcrumb = Breadcrumb(null, null), val label: String? = null)

internal class Breadcrumb(val displayName: String? = null, val groupName: String? = null, val descendants: List<Breadcrumb> = listOf()) {
    constructor(toCopy: Breadcrumb, child: Breadcrumb) : this(toCopy.displayName, toCopy.groupName, toCopy.descendants.toMutableList().apply { add(child) })

    val lastGroupName: String?
        get() = if (descendants.isEmpty()) groupName else descendants.last().groupName
}

internal class NavigationOption(val displayName: String, val groupName: String?)

internal sealed class DisplayItem(val displayName: String?) {
    class Section(displayName: String?) : DisplayItem(displayName)
    class Group(displayName: String?, val group: CockpitParamGroup) : DisplayItem(displayName)
    class Param(displayName: String?, val param: CockpitParam<Any>) : DisplayItem(displayName)
    class Path : DisplayItem(null)
}

internal fun Map<String?, CockpitParamGroup>.toDisplayModel(): DisplayModel {
    val items = mutableListOf<DisplayItem>()
    val crumb = Breadcrumb()
    forEach {
        if (it.key != null) items.addAll(it.value.toDisplayModel(crumb).items)
    }

    if (this.containsKey(null)) items.addAll((this[null]!!.toDisplayModel(crumb).items))

    return DisplayModel(items)
}

internal fun CockpitParamGroup.toDisplayModel(parentCrumb: Breadcrumb, addPath: Boolean = false): DisplayModel {
    val childCrumb = Breadcrumb(displayName, name)
    val newBreadcrumb = Breadcrumb(parentCrumb, childCrumb)
    val items = mutableListOf<DisplayItem>()

    if (!addPath) items.add(DisplayItem.Section(displayName))
    if (addPath) items.add(DisplayItem.Path())

    items.addAll(params.map { DisplayItem.Param(it.description ?: it.name, it) })
    items.addAll(subgroups.map { DisplayItem.Group(it.displayName, it) })

    return DisplayModel(items, newBreadcrumb, displayName)
}
