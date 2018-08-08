package com.polidea.cockpit.core.mapper

import com.polidea.cockpit.core.CockpitParam

class ParamsMapper {

    private val yamlMapToParamMapper = YamlToParamMapper()

    private val paramToYamlMapMapper = ParamToYamlMapper()

    fun toListOfParams(yamlMap: Map<String, Any>): List<CockpitParam<Any>> {
        return yamlMapToParamMapper.toListOfParams(yamlMap)
    }

    fun toYamlMap(params: List<CockpitParam<Any>>): Map<String, Any> {
        return paramToYamlMapMapper.toYamlMap(params)
    }
}