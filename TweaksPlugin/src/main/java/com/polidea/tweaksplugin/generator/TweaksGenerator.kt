package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.kotlinpoet.*
import java.io.File
import kotlin.reflect.KClass


class TweaksGenerator {
    fun generate(params: List<Param<*>>, file: File?) {
        val propertySpecs: ArrayList<PropertySpec> = ArrayList()

        params.map {
            when (it) {
                is BooleanParam -> propertySpecs.add(createPropertySpecForParam(it, Boolean::class))
                is DoubleParam -> propertySpecs.add(createPropertySpecForParam(it, Double::class))
                is IntegerParam -> propertySpecs.add(createPropertySpecForParam(it, Int::class))
                is StringParam -> propertySpecs.add(createPropertySpecForParam(it, String::class))
                else -> throw IllegalArgumentException("Param type undefined: $it!")
            }
        }

        val tweaksFile = FileSpec.builder("com.polidea.androidtweaks.tweaks", "Tweaks")
                .addStaticImport("com.polidea.androidtweaks.manager", "TweaksManager")
                .addType(TypeSpec.classBuilder("Tweaks")
                        .addModifiers(KModifier.OPEN)
                        .addProperties(propertySpecs)
                        .build())

        if (file != null)
            tweaksFile.build().writeTo(file)
        else
            tweaksFile.build().writeTo(System.out)
    }

    private fun createPropertySpecForParam(param: Param<*>, typeClass: KClass<*>): PropertySpec {
        return PropertySpec.builder(param.name, typeClass, KModifier.PUBLIC)
                .mutable(true)
                .getter(FunSpec.getterBuilder()
                        .addStatement("return TweaksManager.getInstance().getParamValue(\"${param.name}\") as ${typeClass.simpleName}")
                        .build())
                .setter(FunSpec.setterBuilder()
                        .addStatement("TweaksManager.getInstance().setParamValue(\"${param.name}\", value)")
                        .addParameter(ParameterSpec.builder("value", typeClass)
                                .build())
                        .build())
                .build()
    }
}
