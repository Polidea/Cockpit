package com.polidea.cockpit.extensions

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.CockpitParamGroup
import com.polidea.cockpit.core.GROUP_DELIMITER
import com.polidea.cockpit.core.MutableCockpitParamGroup

internal fun Map<String?, List<CockpitParam<Any>>>.convertToGroups(): Map<String?, CockpitParamGroup> {
    val extractedSubgroups = mutableMapOf<String?, MutableCockpitParamGroup>()
    forEach {
        val group = extractedSubgroups.getOrCreateGroupHierarchy(it.key)
        group.params.addAll(it.value)
        extractedSubgroups[it.key] = group
    }
    return extractedSubgroups.toReducedNonMutable()
}

private fun MutableMap<String?, MutableCockpitParamGroup>.getOrCreateGroupHierarchy(name: String?): MutableCockpitParamGroup {
    val group = this[name] ?: MutableCockpitParamGroup(name)
    if (!this.containsKey(name)) {
        this[name] = group
    } else {
        return group
    }

    if (name?.contains(GROUP_DELIMITER) == true) {
        val nextName = name.substring(0, name.lastIndexOf(GROUP_DELIMITER))
        val parent = getOrCreateGroupHierarchy(nextName)
        parent.subgroups.add(group)
    }

    return group
}

private fun MutableMap<String?, MutableCockpitParamGroup>.toReducedNonMutable(): Map<String?, CockpitParamGroup> {
    val mapOfNonMutables = mutableMapOf<String?, CockpitParamGroup>()
    topLevelGroups().forEach {
        mapOfNonMutables[it.key] = it.value.toReadOnly()
        mapOfNonMutables.storeSubgroupsRefs(mapOfNonMutables[it.key]!!)
    }

    return mapOfNonMutables
}

private fun MutableMap<String?, CockpitParamGroup>.storeSubgroupsRefs(subgroup: CockpitParamGroup) {
    subgroup.subgroups.forEach {
        this[it.name] = it
        storeSubgroupsRefs(it)
    }
}

internal fun <T> Map<String?, T>.topLevelGroups(): Map<String?, T> = filter { it.key?.contains(GROUP_DELIMITER) != true }