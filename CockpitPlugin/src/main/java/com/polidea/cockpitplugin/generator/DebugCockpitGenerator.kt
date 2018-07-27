package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier

internal class DebugCockpitGenerator : BaseCockpitGenerator() {

    override fun generate(params: List<CockpitParam<*>>, file: File?) {
        val propertyMethods = params.fold(mutableListOf<MethodSpec>()) { acc, param ->
            acc.apply {
                val paramName = param.name
                val paramValue = param.value
                when (paramValue) {
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
                        add(createGetterMethodSpecForParam(paramName, paramValue))
                        add(createSetterMethodSpecForParam(paramName, paramValue))
                        add(createAddPropertyChangeListenerMethodSpecForParam(paramName, paramValue))
                        add(createRemovePropertyChangeListenerMethodSpecForParam(paramName, paramValue))
                    }
                }
            }
        }

        generate(file) { builder ->
            builder.addMethods(propertyMethods)
                    .addFields(generateFields(params))
                    .addMethod(generateShowCockpitMethod())
        }
    }

    private fun generateFields(params: List<CockpitParam<*>>): MutableIterable<FieldSpec> {
        return mutableListOf<FieldSpec>().apply {
            val listenerMapAndColorMapperNeeded = params.find { it.value is CockpitColor } != null
            if (listenerMapAndColorMapperNeeded) {
                add(createColorListenerMapFieldSpec())
                add(createColorColorMapperFieldSpec())
            }
        }
    }

    internal fun createColorListenerMapFieldSpec(): FieldSpec {
        return FieldSpec.builder(ParameterizedTypeName.get(mapClassName,
                getParametrizedCockpitPropertyChangeListenerClassName(String::class.java),
                getParametrizedCockpitPropertyChangeListenerClassName(cockpitColorClassName)),
                "colorListenerMap", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new \$T<>()", hashMapClassName)
                .build()
    }

    internal fun createColorColorMapperFieldSpec(): FieldSpec {
        return FieldSpec.builder(cockpitColorMapperClassName, "cockpitColorMapper",
                Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new \$T()", cockpitColorMapperClassName)
                .build()
    }

    internal fun <T : Any> createGetterMethodSpecForParam(paramName: String, value: T): MethodSpec {
        return createGetterMethodSpecForParamAndConfigurator(paramName, value) { builder ->
            when (value) {
                is CockpitColor -> {
                    val cockpitColorClass = CockpitColor::class.java
                    builder.addStatement("\$T cockpitColor = (\$T) \$T.INSTANCE.getParamValue(\"$paramName\")",
                            cockpitColorClass, cockpitColorClass, cockpitManagerClassName)
                    builder.addStatement("return cockpitColorMapper.unwrap(cockpitColor)")
                }
                else -> {
                    builder.addStatement("return (\$T) \$T.INSTANCE.getParamValue(\"$paramName\")",
                            mapToTypeClass(value), cockpitManagerClassName)
                }
            }
        }
    }

    internal fun createSetterMethodSpecForParam(paramName: String, paramValue: Any): MethodSpec {
        val typeClass = when (paramValue) {
            is CockpitColor -> String::class.java
            else -> mapToTypeClass(paramValue)
        }
        val builder = MethodSpec.methodBuilder("set${paramName.capitalize()}")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeClass, paramName).build())
        when (paramValue) {
            is CockpitColor -> {
                builder.addStatement("\$T cockpitColor = cockpitColorMapper.wrap(color)", cockpitColorClassName)
                builder.addStatement("\$T.INSTANCE.setParamValue(\"$paramName\", cockpitColor)", cockpitManagerClassName)
            }
            else -> builder.addStatement("\$T.INSTANCE.setParamValue(\"$paramName\", $paramName)",
                    cockpitManagerClassName)
        }
        return builder.build()
    }

    internal fun createAddPropertyChangeListenerMethodSpecForParam(paramName: String, paramValue: Any): MethodSpec {
        val typeClass = when (paramValue) {
            is CockpitColor -> String::class.java
            else -> mapToJavaObjectTypeClass(paramValue)
        }
        val listenerParamName = "listener"
        val builder = MethodSpec.methodBuilder("addOn${paramName.capitalize()}ChangeListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitPropertyChangeListenerClassName(typeClass), listenerParamName).build())
        when (paramValue) {
            is CockpitColor -> {
                builder.addStatement("\$T colorListener = new \$T($listenerParamName, cockpitColorMapper)",
                        getParametrizedCockpitPropertyChangeListenerClassName(cockpitColorClassName),
                        ParameterizedTypeName.get(mappingPropertyChangeListenerClassName, cockpitColorClassName, TypeName.get(String::class.java)))
                builder.addStatement("colorListenerMap.put($listenerParamName, colorListener)")
                builder.addStatement("\$T.INSTANCE.addOnParamChangeListener(\"$paramName\", colorListener)", cockpitManagerClassName)
            }
            else -> builder.addStatement("\$T.INSTANCE.addOnParamChangeListener(\"$paramName\", $listenerParamName)",
                    cockpitManagerClassName)
        }
        return builder
                .build()
    }

    internal fun createRemovePropertyChangeListenerMethodSpecForParam(paramName: String, paramValue: Any): MethodSpec {
        val typeClass = when (paramValue) {
            is CockpitColor -> String::class.java
            else -> mapToJavaObjectTypeClass(paramValue)
        }
        val listenerParamName = "listener"
        val builder = MethodSpec.methodBuilder("removeOn${paramName.capitalize()}ChangeListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitPropertyChangeListenerClassName(typeClass), listenerParamName).build())
        when (paramValue) {
            is CockpitColor -> {
                builder.addStatement("\$T colorListener = colorListenerMap.get($listenerParamName)",
                        getParametrizedCockpitPropertyChangeListenerClassName(cockpitColorClassName))
                builder.addStatement("colorListenerMap.remove($listenerParamName)")
                builder.addStatement("\$T.INSTANCE.removeOnParamChangeListener(\"$paramName\", colorListener)",
                        cockpitManagerClassName)
            }
            else -> builder.addStatement("\$T.INSTANCE.removeOnParamChangeListener(\"$paramName\", $listenerParamName)",
                    cockpitManagerClassName)
        }
        return builder.build()
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
                .beginControlFlow("if (fragmentManager.findFragmentByTag(\"Cockpit\") == null)")
                .addStatement("\$T cockpitDialog = \$T.Companion.newInstance()",
                        cockpitDialogClassName, cockpitDialogClassName)
                .addStatement("cockpitDialog.show(fragmentManager, \"Cockpit\")")
                .endControlFlow()
                .build()
    }
}
