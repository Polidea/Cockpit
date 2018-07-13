package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.squareup.javapoet.MethodSpec
import java.io.File

class ReleaseCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.fold(mutableListOf<MethodSpec>()) { acc, param ->
            acc.apply {
                System.out.println(param.name + " " + param.value::class.java)
                when (param.value) {
                    is CockpitAction -> Unit // we don't need getter for action
                    else -> add(createGetterMethodSpecForParam(param))
                }
            }
        }
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