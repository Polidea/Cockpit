package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.kotlinpoet.*
import java.io.File
import kotlin.reflect.KClass

class TweaksInitializerGenerator {
    fun generate(params: List<Param<*>>, file: File?) {

        val initParamsCodeBlock: CodeBlock.Builder = CodeBlock.builder()

        for (p in params) {
            val clazz: KClass<Any>? = p.value?.javaClass?.kotlin
            if (clazz != String::class) {
                initParamsCodeBlock.add("TweaksManager.getInstance().addParam(TweakParam(\"${p.name}\", ${clazz?.simpleName}::class, ${p.value}))\n")
            } else {
                initParamsCodeBlock.add("TweaksManager.getInstance().addParam(TweakParam(\"${p.name}\", ${clazz.simpleName}::class, \"${p.value}\"))\n")
            }
        }

        val initializerFile = FileSpec.builder("com.polidea.androidtweaks.tweaks", "TweaksInitializer")
                .addStaticImport("com.polidea.androidtweaks.manager", "TweaksManager", "TweakParam")
                .addType(TypeSpec.classBuilder("TweaksInitializer")
                        .addFunction(FunSpec.builder("init")
                                .addCode(initParamsCodeBlock.build())
                                .build())
                        .build())

        if (file != null) {
            initializerFile.build().writeTo(file)
        } else {
            initializerFile.build().writeTo(System.out)
        }
    }
}