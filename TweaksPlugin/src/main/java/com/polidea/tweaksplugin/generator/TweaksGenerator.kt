package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.kotlinpoet.*
import java.io.File
import kotlin.reflect.KClass


class TweaksGenerator {
    fun generate(params: List<Param<*>>, file: File?) {
        val initParamsCodeBlock: CodeBlock.Builder = CodeBlock.builder()

        val propertySpecs: ArrayList<PropertySpec> = ArrayList()

        params.map {
            when (it) {
                is BooleanParam -> propertySpecs.add(createPropertySpecForParam(it, Boolean::class))
                is DoubleParam -> propertySpecs.add(createPropertySpecForParam(it, Double::class))
                is IntegerParam -> propertySpecs.add(createPropertySpecForParam(it, Int::class))
                is StringParam -> propertySpecs.add(createPropertySpecForParam(it, String::class))
                else -> throw IllegalArgumentException("Param type undefined: $it!")
            }

            val clazz: KClass<Any>? = it.value?.javaClass?.kotlin
            if (clazz != String::class) {
                initParamsCodeBlock.add("TweaksManager.getInstance().addParam(TweakParam(\"${it.name}\", ${clazz?.simpleName}::class, ${it.value}))\n")
            } else {
                initParamsCodeBlock.add("TweaksManager.getInstance().addParam(TweakParam(\"${it.name}\", ${clazz.simpleName}::class, \"${it.value}\"))\n")
            }
        }

        val tweaksFile = FileSpec.builder("com.polidea.androidtweaks.tweaks", "Tweaks")
                .addStaticImport("com.polidea.androidtweaks.manager", "TweaksManager", "TweakParam")
                .addType(TypeSpec.classBuilder("Tweaks")
                        .addModifiers(KModifier.OPEN)
                        .companionObject(TypeSpec.companionObjectBuilder(null)
                                .addFunction(createGetAllTweaksMethod())
                                .addInitializerBlock(initParamsCodeBlock.build())
                                .addProperties(propertySpecs).build())
                        .build())

        if (file != null)
            tweaksFile.build().writeTo(file)
        else
            tweaksFile.build().writeTo(System.out)
    }

    private fun createPropertySpecForParam(param: Param<*>, typeClass: KClass<*>): PropertySpec {
        return PropertySpec.builder(param.name, typeClass, KModifier.PUBLIC)
                .addAnnotation(JvmStatic::class)
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

    private fun createGetAllTweaksMethod(): FunSpec {
        val tweakParamClass: ClassName = ClassName.bestGuess("com.polidea.androidtweaks.manager.TweakParam")
        val listClass: ClassName = ClassName.bestGuess("kotlin.collections.List")
        val parametrizedListClass: TypeName = ParameterizedTypeName.get(listClass, tweakParamClass)

        return FunSpec.builder("getAllTweaks")
                .addAnnotation(JvmStatic::class)
                .returns(parametrizedListClass)
                .addStatement("return TweaksManager.getInstance().params")
                .build()
    }
}
