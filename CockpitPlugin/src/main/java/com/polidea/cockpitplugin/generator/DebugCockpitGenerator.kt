package com.polidea.cockpitplugin.generator

import com.polidea.cockpitplugin.core.CockpitParam
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import java.io.File
import javax.lang.model.element.Modifier


class DebugCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.fold(ArrayList<MethodSpec>()) { acc, param ->
            acc.apply {
                add(createGetterMethodSpecForParam(param))
                add(createSetterMethodSpecForParam(param))
                add(createAddPropertyChangeListenerMethodSpecForParam(param))
                add(createRemovePropertyChangeListenerMethodSpecForParam(param))
            }
        }
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addMethod(generateShowCockpitMethod())
        }
    }

    internal fun <T : Any> createGetterMethodSpecForParam(param: CockpitParam<T>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            builder.addStatement("return (\$T) \$T.INSTANCE.getParamValue(\"${param.name}\")",
                    mapToTypeClass(param), cockpitManagerClassName)
        }
    }


    internal fun createSetterMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        val typeClass = mapToTypeClass(param)
        return MethodSpec.methodBuilder("set${param.name.capitalize()}")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeClass, param.name).build())
                .addStatement("\$T.INSTANCE.setParamValue(\"${param.name}\", ${param.name})",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createAddPropertyChangeListenerMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        val typeClass = mapToJavaObjectTypeClass(param)
        val listenerParamName = "listener"
        return MethodSpec.methodBuilder("addOn${param.name.capitalize()}ChangeListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitPropertyChangeListenerClassName(typeClass), listenerParamName).build())
                .addStatement("\$T.INSTANCE.addOnParamChangeListener(\"${param.name}\", $listenerParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createRemovePropertyChangeListenerMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        val typeClass = mapToJavaObjectTypeClass(param)
        val listenerParamName = "listener"
        return MethodSpec.methodBuilder("removeOn${param.name.capitalize()}ChangeListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitPropertyChangeListenerClassName(typeClass), listenerParamName).build())
                .addStatement("\$T.INSTANCE.removeOnParamChangeListener(\"${param.name}\", $listenerParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun generateShowCockpitMethod(): MethodSpec {
        return MethodSpec.methodBuilder("showCockpit")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(androidFragmentManagerClassName, "fragmentManager").build())
                .addStatement("\$T cockpitDialog = \$T.Companion.newInstance()",
                        cockpitDialogClassName, cockpitDialogClassName)
                .addStatement("cockpitDialog.show(fragmentManager, \"Cockpit\")")
                .build()
    }
}
