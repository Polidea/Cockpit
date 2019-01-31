package com.polidea.cockpit.core

const val GROUP_DELIMITER = '/'

data class CockpitParamGroup(val name: String?, val subgroups: List<CockpitParamGroup>, val params: List<CockpitParam<Any>>) {
    val displayName: String?
        get() = name?.split(GROUP_DELIMITER)?.last()

    val displaySize: Int
        get() = subgroups.size + params.size

    fun isParent(): Boolean = subgroups.isNotEmpty()
    fun isChild(): Boolean = name?.contains(GROUP_DELIMITER) == true
}

data class MutableCockpitParamGroup(val name: String?,
                                    val subgroups: MutableList<MutableCockpitParamGroup> = mutableListOf(),
                                    val params: MutableList<CockpitParam<Any>> = mutableListOf()) {

    fun toReadOnly(): CockpitParamGroup = CockpitParamGroup(name, subgroups.map { it.toReadOnly() }, params)
}