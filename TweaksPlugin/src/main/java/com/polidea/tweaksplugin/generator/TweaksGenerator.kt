package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.kotlinpoet.*
import java.io.File
import kotlin.reflect.KClass


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

    private val tweakParamClassName = "com.polidea.androidtweaks.manager.$tweakParam"
    private val listClassName = "kotlin.collections.List"
    private val contextClassName = "android.content.Context"
    private val tweaksActivityClassName = "com.polidea.androidtweaks.activity.$tweaksActivity"

    fun generate(params: List<Param<*>>, file: File?) {
        val initParamsCodeBlock: CodeBlock.Builder = CodeBlock.builder()
        initParamsCodeBlock.addStatement("initializeTweaks()")

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

        val tweaksFile = FileSpec.builder(tweaksPackage, tweaks)
                .addStaticImport(tweaksManagerPackage, tweaksManager, tweakParam)
                .addStaticImport(androidContentPackage, intent, context)
                .addStaticImport(tweaksActivityPackage, tweaksActivity)
                .addType(TypeSpec.classBuilder(tweaks)
                        .addModifiers(KModifier.OPEN)
                        .companionObject(TypeSpec.companionObjectBuilder(null)
                                .addFunction(createGetAllTweaksMethod())
                                .addFunction(createInitTweaksMethod(params))
                                .addFunction(generateShowTweaksMethod())
                                .addFunction(generateHideTweaksMethod())
                                .addInitializerBlock(initParamsCodeBlock.build())
                                .addProperties(propertySpecs).build())
                        .build())

        if (file != null)
            tweaksFile.build().writeTo(file)
        else
            tweaksFile.build().writeTo(System.out)
    }

    internal fun createPropertySpecForParam(param: Param<*>, typeClass: KClass<*>): PropertySpec {
        return PropertySpec.builder(param.name, typeClass, KModifier.PUBLIC)
                .addAnnotation(JvmStatic::class)
                .mutable(true)
                .getter(FunSpec.getterBuilder()
                        .addStatement("return $tweaksManager.getInstance().getParamValue(\"${param.name}\") as ${typeClass.simpleName}")
                        .build())
                .setter(FunSpec.setterBuilder()
                        .addStatement("$tweaksManager.getInstance().setParamValue(\"${param.name}\", value)")
                        .addParameter(ParameterSpec.builder("value", typeClass)
                                .build())
                        .build())
                .build()
    }

    internal fun createGetAllTweaksMethod(): FunSpec {
        val tweakParamClass: ClassName = ClassName.bestGuess(tweakParamClassName)
        val listClass: ClassName = ClassName.bestGuess(listClassName)
        val parametrizedListClass: TypeName = ParameterizedTypeName.get(listClass, tweakParamClass)

        return FunSpec.builder("getAllTweaks")
                .addAnnotation(JvmStatic::class)
                .returns(parametrizedListClass)
                .addStatement("return $tweaksManager.getInstance().params")
                .build()
    }

    internal fun createInitTweaksMethod(params: List<Param<*>>): FunSpec {
        val funSpec = FunSpec.builder("initializeTweaks")
                .addModifiers(KModifier.PRIVATE)

        params.forEach {
            val clazz: KClass<Any>? = it.value?.javaClass?.kotlin
            if (clazz != String::class) {
                funSpec.addStatement("$tweaksManager.getInstance().addParam($tweakParam(\"${it.name}\", ${clazz?.simpleName}::class, ${it.value}))")
            } else {
                funSpec.addStatement("$tweaksManager.getInstance().addParam($tweakParam(\"${it.name}\", ${clazz.simpleName}::class, \"${it.value}\"))")
            }
        }

        return funSpec.build()
    }

    internal fun generateShowTweaksMethod(): FunSpec {
        val contextClass: ClassName = ClassName.bestGuess(contextClassName)

        return FunSpec.builder("showTweaks")
                .addAnnotation(JvmStatic::class)
                .addParameter("context", contextClass)
                .addStatement("val intent = Intent(context, $tweaksActivity::class.java)")
                .addStatement("context.startActivity(intent)")
                .build()
    }

    internal fun generateHideTweaksMethod(): FunSpec {
        val tweaksActivityClass: ClassName = ClassName.bestGuess(tweaksActivityClassName)

        return FunSpec.builder("hideTweaks")
                .addAnnotation(JvmStatic::class)
                .addParameter("tweaksActivity", tweaksActivityClass)
                .addStatement("tweaksActivity.finish()")
                .build()
    }
}
