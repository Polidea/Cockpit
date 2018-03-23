package com.polidea.cockpitplugin.generator

import com.polidea.cockpitplugin.model.Param
import com.squareup.javapoet.MethodSpec
import java.io.File

class ReleaseCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<Param<*>>, file: File?) {
        val propertyMethods = params.map { createGetterMethodSpecForParam(it) }
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addMethod(createGetAllCockpitParamsMethod(params))
        }
    }

    internal fun createGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return ${createWrappedValueForParam(param)}")
        }
    }

    internal fun createGetAllCockpitParamsMethod(params: List<Param<*>>): MethodSpec {
        return createGetAllCockpitParamsMethodForConfigurator { builder ->
            builder.addStatement("\$T<\$T> cockpit = new \$T<>()", listClassName, cockpitParamClassName, arrayListClassName)
            params.forEach {
                builder.addStatement("cockpit.add(${createNewCockpitParamStatementForParam(it)})")
            }
            builder.addStatement("return cockpit")
        }
    }
}