package com.polidea.cockpitplugin.generator

import com.polidea.cockpitplugin.model.*
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier


class DebugCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<Param<*>>, file: File?) {
        val propertyMethods = params.fold(ArrayList<MethodSpec>(), { acc, param ->
            acc.add(createGetterMethodSpecForParam(param))
            acc.add(createSetterMethodSpecForParam(param))
            acc
        })
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addMethod(generateShowCockpitMethod())
                    .addMethod(generatePersistChangesMethod())
        }
    }

    internal fun createGetterMethodSpecForParam(param: Param<*>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return (\$T) \$T.INSTANCE.getParamValue(\"${param.name}\")",
                    mapToTypeClass(param), cockpitManagerClassName)
        }
    }


    internal fun createSetterMethodSpecForParam(param: Param<*>): MethodSpec {
        val typeClass = mapToTypeClass(param)
        return MethodSpec.methodBuilder("set${param.name.capitalize()}")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeClass, param.name).build())
                .addStatement("\$T.INSTANCE.setParamValue(\"${param.name}\", ${param.name})",
                        cockpitManagerClassName)
                .addStatement("persistChanges()")
                .build()
    }

    internal fun generateShowCockpitMethod(): MethodSpec {
        return MethodSpec.methodBuilder("showCockpit")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(androidContextClassName, "context").build())
                .addStatement("\$T intent = new \$T(context, \$T.class)",
                        androidIntentClassName, androidIntentClassName, cockpitActivityClassName)
                .addStatement("context.startActivity(intent)")
                .build()
    }

    internal fun generatePersistChangesMethod(): MethodSpec {
        return MethodSpec.methodBuilder("persistChanges")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addStatement("\$T.INSTANCE.saveCockpitAsYaml()", fileUtilsClassName)
                .build()
    }
}
