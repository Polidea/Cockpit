package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier

abstract class BaseTweaksGenerator {

    abstract fun generate(params: List<Param<*>>, file: File?)

    protected val tweaksPackage = "com.polidea.androidtweaks.tweaks"
    protected val tweaksManagerPackage = "com.polidea.androidtweaks.manager"
    protected val androidContentPackage = "android.content"
    protected val tweaksActivityPackage = "com.polidea.androidtweaks.activity"
    protected val javaUtilPackage = "java.util"

    protected val tweaks = "Tweaks"
    protected val tweaksManager = "TweaksManager"
    protected val tweakParam = "TweakParam"
    protected val intent = "Intent"
    protected val context = "Context"
    protected val tweaksActivity = "TweaksActivity"
    protected val list = "List"
    protected val arrayList = "ArrayList"

    protected val tweaksManagerClassName = ClassName.get(tweaksManagerPackage, tweaksManager)
    protected val tweaksParamClassName = ClassName.get(tweaksManagerPackage, tweakParam)
    protected val androidIntentClassName = ClassName.get(androidContentPackage, intent)
    protected val androidContextClassName = ClassName.get(androidContentPackage, context)
    protected val tweaksActivityClassName = ClassName.get(tweaksActivityPackage, tweaksActivity)
    protected val listClassName = ClassName.get(javaUtilPackage, list)
    protected val arrayListClassName = ClassName.get(javaUtilPackage, arrayList)

    protected fun generate(file: File?, configurator: (TypeSpec.Builder) -> TypeSpec.Builder) {

        val tweaksClass = configurator(TypeSpec.classBuilder(tweaks)
                .addModifiers(Modifier.PUBLIC))
                .build()

        val tweaksFile = JavaFile.builder(tweaksPackage, tweaksClass).build()

        if (file == null) {
            tweaksFile.writeTo(System.out)
        } else {
            tweaksFile.writeTo(file)
        }
    }

    inline protected fun createGetterMethodSpecForParamAndConfigurator(param: Param<*>,
                                                                     configurator: (MethodSpec.Builder) -> MethodSpec.Builder): MethodSpec {
        return configurator(MethodSpec.methodBuilder("get${param.name}")
                .returns(mapToTypeClass(param))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC))
                .build()
    }

    inline protected fun createGetAllTweaksMethodForConfigurator(configurator: (MethodSpec.Builder) -> MethodSpec.Builder): MethodSpec {
        val parametrizedListClass: TypeName = ParameterizedTypeName.get(listClassName, tweaksParamClassName)
        return configurator(MethodSpec.methodBuilder("getAllTweaks")
                .returns(parametrizedListClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC))
                .build()
    }

    protected fun createNewTweakParamStatementForParam(it: Param<*>) =
            "new $tweakParam(\"${it.name}\", ${it.value.javaClass.simpleName}.class, ${createWrappedValueForParam(it)})"

    protected fun mapToTypeClass(param: Param<*>): Class<*> {
        return when (param) {
            is BooleanParam -> Boolean::class.java
            is DoubleParam -> Double::class.java
            is IntegerParam -> Int::class.java
            is StringParam -> String::class.java
            else -> throw IllegalArgumentException("Param type undefined: $param!")
        }
    }

    protected fun createWrappedValueForParam(param: Param<*>): Any {
        return when (param.value.javaClass) {
            String::class.java -> "\"${param.value}\""
            else -> param.value
        }
    }

}