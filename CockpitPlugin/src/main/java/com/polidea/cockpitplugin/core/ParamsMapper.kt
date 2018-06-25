package com.polidea.cockpitplugin.core

// TODO: extract to core library
class ParamsMapper {

    fun toMapOfParams(yamlMap: Map<String, Any>): Map<String, YamlParam<*>> {
        // TODO: move deserializing to library, or use different library, if snakeyaml doesn't handle custom objects properly
        return mapOf(*yamlMap.map {
            val value = it.value
            val param = when (value) {
            // new extended yaml format
                is Map<*, *> -> YamlParam(
                        value[KEY_DESCRIPTION] as String?,
                        value[KEY_VALUE] as Any,
                        value[KEY_GROUP] as String?)
            // previous simple yaml format
                else -> YamlParam(null, value, null)
            }
            Pair(it.key, param)
        }.toTypedArray())
    }

    fun toMapOfMaps(params: Map<String, YamlParam<*>>): Map<String, Any> {
        // TODO: move serializing to library, or use different library, if snakeyaml doesn't handle custom objects properly
        return linkedMapOf(*params.map {
            val param = it.value
            if (param.description == null && param.group == null) { // simple parameter with value only
                Pair(it.key, param.value)
            } else { // parameter with description or group (or both of them)
                val map = linkedMapOf<String, Any>()
                param.description?.let { map[KEY_DESCRIPTION] = it }
                param.value.let { map[KEY_VALUE] = it }
                param.group?.let { map[KEY_GROUP] = it }
                Pair(it.key, map)
            }
        }.toTypedArray())
    }

    companion object {
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_VALUE = "value"
        private const val KEY_GROUP = "group"
    }
}