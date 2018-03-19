package com.polidea.tweaksplugin.generator

import com.polidea.tweaksplugin.model.*
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier


class DebugTweaksGenerator : BaseTweaksGenerator() {

    override fun generate(params: List<Param<*>>, file: File?) {
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

    internal fun createGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return (\$T) \$T.getInstance().getParamValue(\"${param.name}\")",
                    mapToTypeClass(param), tweaksManagerClassName)
        }
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

    internal fun createInitTweaksMethod(params: List<Param<*>>): MethodSpec {
        val funSpec = MethodSpec.methodBuilder("initializeTweaks")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)

        params.forEach {
            funSpec.addStatement("$tweaksManager.getInstance().addParam(${createNewTweakParamStatementForParam(it)})")
        }

        return funSpec.build()
    }

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
}
