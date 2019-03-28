package com.polidea.cockpit.extensions

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.core.GROUP_DELIMITER
import com.polidea.cockpit.core.MutableCockpitParamGroup

internal fun Map<String?, CockpitParamGroup>.getDisplaySize(): Int {
    return this.values.fold(0) { acc, group ->
        acc + if (!group.isChild()) 1 + group.displaySize else 0
    }
}

internal fun Map<String?, List<CockpitParam<Any>>>.convertToGroups(): Map<String?, CockpitParamGroup> {
    val extractedSubgroups = LinkedHashMap<String?, MutableCockpitParamGroup>()

    forEach {
        val group = extractedSubgroups.getOrCreateGroupAndParents(it.key)
        group.params.addAll(it.value)
        extractedSubgroups[it.key] = group
    }

    return extractedSubgroups.toReducedNonMutable()
}

private fun MutableMap<String?, MutableCockpitParamGroup>.getOrCreateGroupAndParents(name: String?): MutableCockpitParamGroup {
    val group = this[name] ?: MutableCockpitParamGroup(name)
    if (!this.containsKey(name)) this[name] = group

    if (name?.contains(GROUP_DELIMITER) == true) {
        val nextName = name.substring(0, name.lastIndexOf(GROUP_DELIMITER))
        val parent = getOrCreateGroupAndParents(nextName)
        parent.subgroups.add(group)
    }

    return group
}

private fun MutableMap<String?, MutableCockpitParamGroup>.toReducedNonMutable(): Map<String?, CockpitParamGroup> {
    val mapOfNonMutables = LinkedHashMap<String?, CockpitParamGroup>()
    topLevelGroups().forEach {
        mapOfNonMutables[it.key] = it.value.toReadOnly()
        mapOfNonMutables[it.key]?.subgroups?.forEach { subgroup ->
            mapOfNonMutables[subgroup.name] = subgroup
        }
    }

    assert(this.size == mapOfNonMutables.size)

    return mapOfNonMutables
}

internal fun <T> Map<String?, T>.topLevelGroups(): Map<String?, T> = filter { it.key?.contains(GROUP_DELIMITER) != true }