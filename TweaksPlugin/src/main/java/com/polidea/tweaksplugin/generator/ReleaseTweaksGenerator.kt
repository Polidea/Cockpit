package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.Param
import com.squareup.javapoet.MethodSpec
import java.io.File

class ReleaseTweaksGenerator: BaseTweaksGenerator() {

    override fun generate(params: List<Param<*>>, file: File?) {
        val propertyMethods = params.map { createGetterMethodSpecForParam(it) }
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addMethod(createGetAllTweaksMethod(params))
        }
    }

    internal fun createGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return ${createWrappedValueForParam(param)}")
        }
    }

    internal fun createGetAllTweaksMethod(params: List<Param<*>>): MethodSpec {
        return createGetAllTweaksMethodForConfigurator { builder ->
            builder.addStatement("\$T<\$T> tweaks = new \$T<>()", listClassName, tweaksParamClassName, arrayListClassName)
            params.forEach {
                builder.addStatement("tweaks.add(${createNewTweakParamStatementForParam(it)})")
            }
            builder.addStatement("return tweaks")
        }
    }
}