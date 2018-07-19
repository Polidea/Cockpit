package com.polidea.cockpit.core.mapper

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType

internal class ParamToYamlMapper {

    fun toYamlMap(params: List<CockpitParam<Any>>): Map<String, Any> {
        return linkedMapOf(*params.map {
            if (it.description == null && it.group == null) { // simple parameter with value only
                val value = it.value
                when (value) {
                    is CockpitListType<*> -> toExtendedYamlFormat(it)
                    else -> toSimpleYamlFormat(it)
                }
            } else { // parameter with description or group (or both of them)
                toExtendedYamlFormat(it)
            }
        }.toTypedArray())
    }

    private fun toSimpleYamlFormat(it: CockpitParam<Any>) = Pair(it.name, it.value)

    private fun toExtendedYamlFormat(it: CockpitParam<Any>): Pair<String, LinkedHashMap<String, Any>> {
        val map = linkedMapOf<String, Any>()
        val value = it.value
        when (value) {
            is CockpitAction -> {
                map[Consts.KEY_TYPE] = YamlParamType.ACTION.value
                value.buttonText?.let { map[Consts.KEY_ACTION_BUTTON_TEXT] = it }
            }
            is CockpitListType<*> -> {
                map[Consts.KEY_TYPE] = YamlParamType.LIST.value
                map[Consts.KEY_LIST_VALUES] = value.items
                map[Consts.KEY_LIST_SELECTION_INDEX] = value.selectedIndex
            }
            is CockpitColor -> {
                map[Consts.KEY_TYPE] = YamlParamType.COLOR.value
                map[Consts.KEY_VALUE] = value.value
            }
            else -> {
                value.let { map[Consts.KEY_VALUE] = it }
            }
        }
        it.description?.let { map[Consts.KEY_DESCRIPTION] = it }
        it.group?.let { map[Consts.KEY_GROUP] = it }
        return Pair(it.name, map)
    }
}
