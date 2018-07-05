package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.type.core.CockpitListType
import com.squareup.javapoet.MethodSpec
import java.io.File
import javax.lang.model.element.Modifier

internal class ReleaseCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.fold(mutableListOf<MethodSpec>()) { acc, param ->
            acc.apply {
                when (param.value) {
                    is CockpitAction -> Unit // we don't need getter for action
                    is CockpitListType<*> -> add(createSelectedValueGetterMethodSpecForParam(param as CockpitParam<CockpitListType<*>>))
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
            val value = param.value
            when (value) {
                is List<*> -> builder.addStatement(createArrayListStatement(value as ArrayList<*>).toString(), arraysClassName)
                else -> builder.addStatement("return ${createWrappedValueForParamValue(param.value)}")
            }
        }
    }

    private fun createArrayListStatement(value: ArrayList<*>): StringBuilder {
        val stringBuilder = StringBuilder()
        stringBuilder.append("return new ArrayList<>(\$T.asList(")

        val itemSeparator = ", "
        value.forEach {
            it?.let {
                stringBuilder.append(createWrappedValueForParamValue(it))
            } ?: stringBuilder.append("null")
            stringBuilder.append(itemSeparator)
        }
        val lastIndexOf = stringBuilder.lastIndexOf(itemSeparator)
        if (lastIndexOf != -1) {
            stringBuilder.delete(lastIndexOf, lastIndexOf + itemSeparator.length) // remove comma and space after last value
        }
        stringBuilder.append("))")

        return stringBuilder
    }

    internal fun createSelectedValueGetterMethodSpecForParam(param: CockpitParam<CockpitListType<*>>): MethodSpec {
        val returnValue: String = if (param.value.items[0] is String) {
            "\"${param.value.items[param.value.selectedIndex]}\""
        } else {
            "${param.value.items[param.value.selectedIndex]}"
        }

        return MethodSpec.methodBuilder("get${param.name.capitalize()}SelectedValue")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(param.value.items[0]::class.java)
                .addStatement("return $returnValue")
                .build()
    }

    private fun createWrappedValueForParamValue(value: Any): Any {
        return when (value.javaClass) {
            String::class.java -> "\"$value\""
            else -> value
        }
    }
}