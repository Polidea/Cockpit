package com.polidea.cockpitplugin.core

// TODO: extract to core library
class ParamsMapper {

    fun toListOfParams(yamlMap: Map<String, Any>): List<Param<*>> {
        // TODO: move deserializing to library, or use different library, if snakeyaml doesn't handle custom objects properly
        return yamlMap.map {
            val value = it.value
            val param = when (value) {
            // new extended yaml format
                is Map<*, *> -> CockpitParam(
                        it.key,
                        value[KEY_VALUE] as Any,
                        value[KEY_DESCRIPTION] as String?,
                        value[KEY_GROUP] as String?)
            // previous simple yaml format
                else -> CockpitParam(it.key, value, null, null)
            }
            param
        }
    }

    fun toYamlMap(params: List<Param<*>>): Map<String, Any> {
        // TODO: move serializing to library, or use different library, if snakeyaml doesn't handle custom objects properly
        return linkedMapOf(*params.map {
            if (it.description == null && it.group == null) { // simple parameter with value only
                Pair(it.name, it.value)
            } else { // parameter with description or group (or both of them)
                val map = linkedMapOf<String, Any>()
                it.description?.let { map[KEY_DESCRIPTION] = it }
                it.value.let { map[KEY_VALUE] = it }
                it.group?.let { map[KEY_GROUP] = it }
                Pair(it.name, map)
            }
        }.toTypedArray())
    }

    companion object {
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_VALUE = "value"
        private const val KEY_GROUP = "group"
    }
}