package com.polidea.cockpit.paramsedition

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup

internal class DisplayModel(val items: List<DisplayItem>, val breadcrumb: Breadcrumb = Breadcrumb(), val label: String? = null)

internal class Breadcrumb(val crumbs: List<String> = listOf()) {
    constructor(crumb: String, previousCrumb: Breadcrumb) : this(previousCrumb.crumbs.toMutableList().apply { add(crumb) })
}

internal sealed class DisplayItem(val displayName: String?) {
    class Section(displayName: String?) : DisplayItem(displayName)
    class Group(displayName: String?, val group: CockpitParamGroup) : DisplayItem(displayName)
    class Param(displayName: String?, val param: CockpitParam<Any>) : DisplayItem(displayName)
    class Path(val breadcrumb: Breadcrumb) : DisplayItem(null)
}

internal fun Map<String?, CockpitParamGroup>.toDisplayModel(): DisplayModel {
    val items = mutableListOf<DisplayItem>()
    forEach {
        items.addAll(it.value.toDisplayModel(Breadcrumb()).items)
    }

    return DisplayModel(items)
}

internal fun CockpitParamGroup.toDisplayModel(breadcrumb: Breadcrumb, addPath: Boolean = false): DisplayModel {
    val newBreadcrumb = Breadcrumb(name ?: "", breadcrumb)

    val items = mutableListOf<DisplayItem>(DisplayItem.Section(displayName))

    if (addPath) items.add(DisplayItem.Path(newBreadcrumb))

    items.addAll(subgroups.map { DisplayItem.Group(it.displayName, it) })
    items.addAll(params.map { DisplayItem.Param(it.description ?: it.name, it) })

    return DisplayModel(items, newBreadcrumb, displayName)
}
