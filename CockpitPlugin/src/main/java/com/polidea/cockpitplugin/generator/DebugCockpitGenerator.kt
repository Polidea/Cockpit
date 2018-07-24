package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import java.io.File
import javax.lang.model.element.Modifier


internal class DebugCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.fold(mutableListOf<MethodSpec>()) { acc, param ->
            acc.apply {
                when (param.value) {
                    is CockpitAction -> {
                        add(createAddActionRequestCallbackMethodSpecForParam(param))
                        add(createRemoveActionRequestCallbackMethodSpecForParam(param))
                    }
                    is CockpitListType<*> -> {
                        val listTypeParam = param as CockpitParam<CockpitListType<*>>
                        add(createAddSelectionChangeListenerMethodSpecForParam(listTypeParam))
                        add(createRemoveSelectionChangeListenerMethodSpecForParam(listTypeParam))
                        add(createSelectedValueGetterMethodSpecForParam(listTypeParam))
                    }
                    else -> {
                        add(createGetterMethodSpecForParam(param))
                        add(createSetterMethodSpecForParam(param))
                        add(createAddPropertyChangeListenerMethodSpecForParam(param))
                        add(createRemovePropertyChangeListenerMethodSpecForParam(param))
                    }
                }
            }
        }
        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addMethod(generateShowCockpitMethod())
        }
    }

    internal fun <T : Any> createGetterMethodSpecForParam(param: CockpitParam<T>): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(param) { builder ->
            val value = param.value
            when (value) {
                is CockpitColor -> {
                    val cockpitColorClass = CockpitColor::class.java
                    builder.addStatement("\$T cockpitColor = (\$T) \$T.INSTANCE.getParamValue(\"${param.name}\")",
                            cockpitColorClass, cockpitColorClass, cockpitManagerClassName)
                    builder.addStatement("return cockpitColor.getValue()")
                }
                else -> {
                    builder.addStatement("return (\$T) \$T.INSTANCE.getParamValue(\"${param.name}\")",
                            mapToTypeClass(param), cockpitManagerClassName)
                }
            }
        }
    }

    internal fun createSetterMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        val typeClass = when (param.value) {
            is CockpitColor -> String::class.java
            else -> mapToTypeClass(param)
        }
        val builder = MethodSpec.methodBuilder("set${param.name.capitalize()}")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeClass, param.name).build())
        when (param.value) {
            is CockpitColor -> {
                val cockpitColorClass = CockpitColor::class.java
                builder.addStatement("\$T cockpitColor = new \$T(${param.name})", cockpitColorClass, cockpitColorClass)
                builder.addStatement("\$T.INSTANCE.setParamValue(\"${param.name}\", cockpitColor)", cockpitManagerClassName)
            }
            else -> builder.addStatement("\$T.INSTANCE.setParamValue(\"${param.name}\", ${param.name})",
                    cockpitManagerClassName)
        }
        return builder.build()
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

    internal fun createAddActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        val callbackParamName = "callback"
        return MethodSpec.methodBuilder("add${param.name.capitalize()}ActionRequestCallback")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(actionRequestCallbackClassName, callbackParamName).build())
                .addStatement("\$T.INSTANCE.addActionRequestCallback(\"${param.name}\", $callbackParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createRemoveActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>): MethodSpec {
        val callbackParamName = "callback"
        return MethodSpec.methodBuilder("remove${param.name.capitalize()}ActionRequestCallback")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(actionRequestCallbackClassName, callbackParamName).build())
                .addStatement("\$T.INSTANCE.removeActionRequestCallback(\"${param.name}\", $callbackParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createAddSelectionChangeListenerMethodSpecForParam(param: CockpitParam<CockpitListType<*>>): MethodSpec {
        val listenerParamName = "listener"
        return MethodSpec.methodBuilder("add${param.name.capitalize()}SelectionChangeListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitSelectionChangeListenerClassName(param.value.getSelectedItem()::class.java), listenerParamName).build())
                .addStatement("\$T.INSTANCE.addSelectionChangeListener(\"${param.name}\", $listenerParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createRemoveSelectionChangeListenerMethodSpecForParam(param: CockpitParam<CockpitListType<*>>): MethodSpec {
        val listenerParamName = "listener"
        return MethodSpec.methodBuilder("remove${param.name.capitalize()}SelectionChangeListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitSelectionChangeListenerClassName(param.value.getSelectedItem()::class.java), listenerParamName).build())
                .addStatement("\$T.INSTANCE.removeSelectionChangeListener(\"${param.name}\", $listenerParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createSelectedValueGetterMethodSpecForParam(param: CockpitParam<CockpitListType<*>>): MethodSpec {
        val returnType = param.value.items[0]::class.java

        return MethodSpec.methodBuilder("get${param.name.capitalize()}SelectedValue")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(returnType)
                .addStatement("return \$T.INSTANCE.getSelectedValue(\"${param.name}\")", cockpitManagerClassName)
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
