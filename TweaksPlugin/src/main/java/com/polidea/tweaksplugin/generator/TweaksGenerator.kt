package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier


class TweaksGenerator {
    private val tweaksPackage = "com.polidea.androidtweaks.tweaks"
    private val tweaksManagerPackage = "com.polidea.androidtweaks.manager"
    private val androidContentPackage = "android.content"
    private val tweaksActivityPackage = "com.polidea.androidtweaks.activity"

    private val tweaks = "Tweaks"
    private val tweaksManager = "TweaksManager"
    private val tweakParam = "TweakParam"
    private val intent = "Intent"
    private val context = "Context"
    private val tweaksActivity = "TweaksActivity"

    private val tweaksManagerClassName = ClassName.get(tweaksManagerPackage, tweaksManager)
    private val tweaksParamClassName = ClassName.get(tweaksManagerPackage, tweakParam)
    private val androidIntentClassName = ClassName.get(androidContentPackage, intent)
    private val androidContextClassName = ClassName.get(androidContentPackage, context)
    private val tweaksActivityClassName = ClassName.get(tweaksActivityPackage, tweaksActivity)

    fun generate(params: List<Param<*>>, file: File?) {
        val propertyMethods = params.fold(ArrayList<MethodSpec>(), { acc, param ->
            acc.add(createGetterMethodSpecForParam(param))
            acc.add(createSetterMethodSpecForParam(param))
            acc
        })

        val tweaksClass = TypeSpec.classBuilder(tweaks)
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(CodeBlock.builder().addStatement("initializeTweaks()").build())
                .addMethod(createInitTweaksMethod(params))
                .addMethods(propertyMethods)
                .addMethod(createGetAllTweaksMethod())
                .addMethod(generateShowTweaksMethod())
                .addMethod(generateHideTweaksMethod())
                .build()

        val tweaksFile = JavaFile.builder(tweaksPackage, tweaksClass).build()

        if (file == null) {
            tweaksFile.writeTo(System.out)
        } else {
            tweaksFile.writeTo(file)
        }
    }

    private fun createGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        val typeClass = mapToTypeClass(param)
        return MethodSpec.methodBuilder("get${param.name}")
                .returns(typeClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return (\$T) \$T.getInstance().getParamValue(\"${param.name}\")",
                        typeClass, tweaksManagerClassName)
                .build()
    }

    private fun createSetterMethodSpecForParam(param: Param<*>): MethodSpec {
        val typeClass = mapToTypeClass(param)
        return MethodSpec.methodBuilder("set${param.name}")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeClass, param.name).build())
                .addStatement("\$T.getInstance().setParamValue(\"${param.name}\", ${param.name})",
                        tweaksManagerClassName)
                .build()
    }

    private fun createGetAllTweaksMethod(): MethodSpec {
        val listClassName = ClassName.get("java.util", "List")
        val parametrizedListClass: TypeName = ParameterizedTypeName.get(listClassName, tweaksParamClassName)
        return MethodSpec.methodBuilder("getAllTweaks")
                .returns(parametrizedListClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return \$T.getInstance().getParams()", tweaksManagerClassName)
                .build()
    }


    private fun createInitTweaksMethod(params: List<Param<*>>): MethodSpec {
        val funSpec = MethodSpec.methodBuilder("initializeTweaks")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)

        params.forEach {
            val clazz = it.value?.javaClass
            val wrappedValue = when (clazz) {
                String::class.java -> "\"${it.value}\""
                else -> it.value
            }
            funSpec.addStatement("$tweaksManager.getInstance().addParam(new $tweakParam(\"${it.name}\", ${clazz?.simpleName}.class, $wrappedValue))")
        }

        return funSpec.build()
    }


    private fun generateShowTweaksMethod(): MethodSpec {
        return MethodSpec.methodBuilder("showTweaks")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(androidContextClassName, "context").build())
                .addStatement("\$T intent = new \$T(context, \$T .class)",
                        androidIntentClassName, androidIntentClassName, tweaksActivityClassName)
                .addStatement("context.startActivity(intent)")
                .build()
    }


    private fun generateHideTweaksMethod(): MethodSpec {
        return MethodSpec.methodBuilder("hideTweaks")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(tweaksActivityClassName, "tweaksActivity").build())
                .addStatement("tweaksActivity.finish()")
                .build()
    }

    private fun mapToTypeClass(param: Param<*>): Class<*> {
        return when (param) {
            is BooleanParam -> Boolean::class.java
            is DoubleParam -> Double::class.java
            is IntegerParam -> Int::class.java
            is StringParam -> String::class.java
            else -> throw IllegalArgumentException("Param type undefined: $param!")
        }
    }


}
