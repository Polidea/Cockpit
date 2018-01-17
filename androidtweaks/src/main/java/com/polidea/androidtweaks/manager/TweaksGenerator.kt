package com.polidea.androidtweaks.manager

import com.polidea.androidtweaks.model.*
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec


class TweaksGenerator {
    fun generate(params: List<Param<*>>) {
        val propertySpecs: ArrayList<PropertySpec> = ArrayList()
        for (p in params) {
            when (p) {
                is BooleanParam -> propertySpecs.add(PropertySpec.builder(p.name, Boolean::class, KModifier.PUBLIC).build())
                is DoubleParam -> propertySpecs.add(PropertySpec.builder(p.name, Double::class, KModifier.PUBLIC).build())
                is IntegerParam -> propertySpecs.add(PropertySpec.builder(p.name, Int::class, KModifier.PUBLIC).build())
                is StringParam -> propertySpecs.add(PropertySpec.builder(p.name, String::class, KModifier.PUBLIC).build())
            }
        }

        val tweaksFile = FileSpec.builder("com.polidea.androidtweaks.tweaks", "Tweaks")
                .addType(TypeSpec.classBuilder("Tweaks")
                    .addProperties(propertySpecs)
                        .build())

        tweaksFile.build().writeTo(System.out) // todo write to the actual file
    }
}