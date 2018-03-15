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
    private val javaUtilPackage = "java.util"

    private val tweaks = "Tweaks"
    private val tweaksManager = "TweaksManager"
    private val tweakParam = "TweakParam"
    private val intent = "Intent"
    private val context = "Context"
    private val tweaksActivity = "TweaksActivity"
    private val list = "List"
    private val arrayList = "ArrayList"

    private val tweaksManagerClassName = ClassName.get(tweaksManagerPackage, tweaksManager)
    private val tweaksParamClassName = ClassName.get(tweaksManagerPackage, tweakParam)
    private val androidIntentClassName = ClassName.get(androidContentPackage, intent)
    private val androidContextClassName = ClassName.get(androidContentPackage, context)
    private val tweaksActivityClassName = ClassName.get(tweaksActivityPackage, tweaksActivity)
    private val listClassName = ClassName.get(javaUtilPackage, list)
    private val arrayListClassName = ClassName.get(javaUtilPackage, arrayList)

    fun generateDebug(params: List<Param<*>>, file: File?) {
        val propertyMethods = params.fold(ArrayList<MethodSpec>(), { acc, param ->
            acc.add(createGetterMethodSpecForParam(param))
            acc.add(createSetterMethodSpecForParam(param))
            acc
        })
        generate(file) { builder ->
            builder.addStaticBlock(CodeBlock.builder().addStatement("initializeTweaks()").build())
                    .addMethod(createInitTweaksMethod(params))
                    .addMethods(propertyMethods)
                    .addMethod(createGetAllTweaksMethod())
                    .addMethod(generateShowTweaksMethod())
                    .addMethod(generateHideTweaksMethod())
        }
    }

    fun generateRelease(params: List<Param<*>>, file: File?) {
        val propertyMethods = params.map { createReleaseGetterMethodSpecForParam(it) }
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addMethod(createReleaseGetAllTweaksMethod(params))
        }
    }

    private fun generate(file: File?, configurator: (TypeSpec.Builder) -> TypeSpec.Builder) {

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


    internal fun createReleaseGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return ${createWrappedValueForParam(param)}")
        }
    }

    internal fun createGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return (\$T) \$T.getInstance().getParamValue(\"${param.name}\")",
                    mapToTypeClass(param), tweaksManagerClassName)
        }
    }

    private fun createGetterMethodSpecForParamAndConfigurator(param: Param<*>,
                                                              configurator: (MethodSpec.Builder) -> MethodSpec.Builder): MethodSpec {
        return configurator(MethodSpec.methodBuilder("get${param.name}")
                .returns(mapToTypeClass(param))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC))
                .build()
    }

    internal fun createSetterMethodSpecForParam(param: Param<*>): MethodSpec {
        val typeClass = mapToTypeClass(param)
        return MethodSpec.methodBuilder("set${param.name}")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeClass, param.name).build())
                .addStatement("\$T.getInstance().setParamValue(\"${param.name}\", ${param.name})",
                        tweaksManagerClassName)
                .build()
    }

    internal fun createGetAllTweaksMethod(): MethodSpec {
        return createGetAllTweaksMethodForConfigurator { builder ->
            builder.addStatement("return \$T.getInstance().getParams()", tweaksManagerClassName)
        }
    }

    internal fun createReleaseGetAllTweaksMethod(params: List<Param<*>>): MethodSpec {
        return createGetAllTweaksMethodForConfigurator { builder ->
            builder.addStatement("\$T<\$T> tweaks = new \$T<>()", listClassName, tweaksParamClassName, arrayListClassName)
            params.forEach {
                builder.addStatement("tweaks.add(${createNewTweakParamStatementForParam(it)})")
            }
            builder.addStatement("return tweaks")
        }
    }

    private fun createGetAllTweaksMethodForConfigurator(configurator: (MethodSpec.Builder) -> MethodSpec.Builder): MethodSpec {
        val parametrizedListClass: TypeName = ParameterizedTypeName.get(listClassName, tweaksParamClassName)
        return configurator(MethodSpec.methodBuilder("getAllTweaks")
                .returns(parametrizedListClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC))
                .build()
    }


    internal fun createInitTweaksMethod(params: List<Param<*>>): MethodSpec {
        val funSpec = MethodSpec.methodBuilder("initializeTweaks")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)

        params.forEach {
            funSpec.addStatement("$tweaksManager.getInstance().addParam(${createNewTweakParamStatementForParam(it)})")
        }

        return funSpec.build()
    }

    private fun createNewTweakParamStatementForParam(it: Param<*>) =
            "new $tweakParam(\"${it.name}\", ${it.value.javaClass.simpleName}.class, ${createWrappedValueForParam(it)})"

    internal fun generateShowTweaksMethod(): MethodSpec {
        return MethodSpec.methodBuilder("showTweaks")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(androidContextClassName, "context").build())
                .addStatement("\$T intent = new \$T(context, \$T .class)",
                        androidIntentClassName, androidIntentClassName, tweaksActivityClassName)
                .addStatement("context.startActivity(intent)")
                .build()
    }


    internal fun generateHideTweaksMethod(): MethodSpec {
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

    private fun createWrappedValueForParam(param: Param<*>): Any {
        return when (param.value.javaClass) {
            String::class.java -> "\"${param.value}\""
            else -> param.value
        }
    }


}
