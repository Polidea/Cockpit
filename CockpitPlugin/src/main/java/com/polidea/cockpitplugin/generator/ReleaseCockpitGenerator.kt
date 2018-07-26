package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType
import com.squareup.javapoet.MethodSpec
import java.io.File
import javax.lang.model.element.Modifier

internal class ReleaseCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.fold(mutableListOf<MethodSpec>()) { acc, param ->
            acc.apply {
                val paramName = param.name
                val value = param.value
                when (value) {
                    is CockpitAction -> Unit // we don't need getter for action
                    is CockpitListType<*> -> add(createSelectedValueGetterMethodSpecForParam(param as CockpitParam<CockpitListType<*>>))
                    else -> add(createGetterMethodSpecForParam(paramName, value))
                }
            }
        }
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
        }
    }

    internal fun createGetterMethodSpecForParam(paramName: String, value: Any): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(paramName, value) { builder ->
            builder.addStatement("return ${createWrappedValueForParamValue(value)}")
        }
    }

    internal fun createSelectedValueGetterMethodSpecForParam(param: CockpitParam<CockpitListType<*>>): MethodSpec {
        val returnValue = createWrappedValueForParamValue(param.value.items[param.value.selectedIndex])

        return MethodSpec.methodBuilder("get${param.name.capitalize()}SelectedValue")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(param.value.items[0]::class.java)
                .addStatement("return $returnValue")
                .build()
    }

    private fun createWrappedValueForParamValue(value: Any): Any {
        return when (value) {
            is String -> "\"$value\""
            is CockpitColor -> "\"${value.value}\""
            else -> value
        }
    }
}