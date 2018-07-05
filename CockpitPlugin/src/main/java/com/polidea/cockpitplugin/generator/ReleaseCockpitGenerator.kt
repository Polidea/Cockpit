package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.squareup.javapoet.MethodSpec
import java.io.File

class ReleaseCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.map { createGetterMethodSpecForParam(it) }

        generate(file) { builder ->
            builder.addMethods(propertyMethods)
        }
    }

    internal fun createGetterMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return ${createWrappedValueForParam(param)}")
        }
    }
}