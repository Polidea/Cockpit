package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.*
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
                        add(createDeprecatedAddActionRequestCallbackMethodSpecForParam(param))
                        add(createLifecycleAcceptingAddActionRequestCallbackMethodSpecForParam(param))
                        add(createAddForeverActionRequestCallbackMethodSpecForParam(param))
                        add(createRemoveActionRequestCallbackMethodSpecForParam(param))
                    }
                    is CockpitListType<*> -> {
                        val listTypeParam = param as CockpitParam<CockpitListType<*>>
                        add(createSelectedValueGetterMethodSpecForParam(listTypeParam))
                        add(createDeprecatedAddPropertyChangeListenerMethodSpecForParam(paramName, listTypeParam.value.getSelectedItem()))
                        add(createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForParam(paramName, listTypeParam.value.getSelectedItem()))
                        add(createAddForeverPropertyChangeListenerMethodSpecForParam(paramName, listTypeParam.value.getSelectedItem()))
                        add(createRemovePropertyChangeListenerMethodSpecForParam(paramName, listTypeParam.value.getSelectedItem()))
                    }
                    is CockpitColor -> {
                        add(createGetterMethodSpecForColorParam(paramName, paramValue))
                        add(createSetterMethodSpecForColorParam(paramName, paramValue))
                        add(createDeprecatedAddPropertyChangeListenerMethodSpecForColorParam(paramName, paramValue))
                        add(createAddForeverPropertyChangeListenerMethodSpecForColorParam(paramName, paramValue))
                        add(createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForColorParam(paramName, paramValue))
                        add(createRemovePropertyChangeListenerMethodSpecForColorParam(paramName, paramValue))
                    }
                    is CockpitRange<*> -> {
                        add(createGetterMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createSetterMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createDeprecatedAddPropertyChangeListenerMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createAddForeverPropertyChangeListenerMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createRemovePropertyChangeListenerMethodSpecForRangeParam(paramName, paramValue.value))
                    }
                    is CockpitStep<*> -> {
                        add(createGetterMethodSpecForStepParam(paramName, paramValue.value))
                        add(createSetterMethodSpecForStepParam(paramName, paramValue.value))
                        add(createDeprecatedAddPropertyChangeListenerMethodSpecForStepParam(paramName, paramValue.value))
                        add(createAddForeverPropertyChangeListenerMethodSpecForStepParam(paramName, paramValue.value))
                        add(createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForStepParam(paramName, paramValue.value))
                        add(createRemovePropertyChangeListenerMethodSpecForStepParam(paramName, paramValue.value))
                    }
                    is CockpitReadOnly ->
                        add(createSetterMethodSpecForReadOnlyParam(paramName, paramValue))
                    else -> {
                        add(createGetterMethodSpecForParam(paramName, paramValue))
                        add(createSetterMethodSpecForParam(paramName, paramValue))
                        add(createDeprecatedAddPropertyChangeListenerMethodSpecForParam(paramName, paramValue))
                        add(createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForParam(paramName, paramValue))
                        add(createAddForeverPropertyChangeListenerMethodSpecForParam(paramName, paramValue))
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
            val colorMapperNeeded = params.find { it.value is CockpitColor } != null
            if (colorMapperNeeded) {
                add(createColorListenerMapFieldSpec())
                add(createColorMapperFieldSpec())
            }

            val cockpitRangeTypes = params.mapNotNull { it.value as? CockpitRange<*> }.map { mapToJavaObjectTypeClass(it.value) }.distinct()
            cockpitRangeTypes.forEach {
                add(createRangeListenerMapFieldSpec(it))
                add(createRangeMapperFieldSpec(it))
            }

            val cockpitStepTypes = params.mapNotNull { it.value as? CockpitStep<*> }.map { mapToJavaObjectTypeClass(it.value) }.distinct()
            cockpitStepTypes.forEach {
                add(createStepListenerMapFieldSpec(it))
                add(createStepMapperFieldSpec(it))
            }

            val readOnlyMapperNeeded = params.find { it.value is CockpitReadOnly } != null
            if (readOnlyMapperNeeded) {
                add(createReadOnlyMapperFieldSpec())
            }
        }
    }

    internal fun createColorListenerMapFieldSpec() =
            createListenerMapFieldSpec(COLOR_LISTENER_MAP,
                    getParametrizedCockpitPropertyChangeListenerClassName(cockpitColorClassName),
                    getParametrizedCockpitPropertyChangeListenerClassName(String::class.java))

    internal fun createRangeListenerMapFieldSpec(paramClass: Class<*>) =
            createListenerMapFieldSpec(getRangeListenerMapName(paramClass),
                    getParametrizedCockpitPropertyChangeListenerClassName(getParametrizedCockpitRangeClassName(paramClass)),
                    getParametrizedCockpitPropertyChangeListenerClassName(paramClass))

    internal fun createStepListenerMapFieldSpec(paramClass: Class<*>) =
            createListenerMapFieldSpec(getStepListenerMapName(paramClass),
                    getParametrizedCockpitPropertyChangeListenerClassName(getParametrizedCockpitStepClassName(paramClass)),
                    getParametrizedCockpitPropertyChangeListenerClassName(paramClass))

    private fun createListenerMapFieldSpec(mapName: String, wrappablePropertyChangeListener: TypeName, paramPropertyChangeListener: TypeName): FieldSpec {
        return FieldSpec.builder(ParameterizedTypeName.get(mapClassName, paramPropertyChangeListener, wrappablePropertyChangeListener),
                mapName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new \$T<>()", hashMapClassName)
                .build()
    }

    internal fun createReadOnlyMapperFieldSpec(): FieldSpec =
            createMapperFieldSpec(cockpitReadOnlyMapperClassName, COCKPIT_READ_ONLY_MAPPER)

    internal fun createColorMapperFieldSpec(): FieldSpec =
            createMapperFieldSpec(cockpitColorMapperClassName, COCKPIT_COLOR_MAPPER)

    internal fun createRangeMapperFieldSpec(paramClass: Class<*>): FieldSpec =
            createMapperFieldSpec(getParametrizedCockpitRangeMapperClassName(paramClass), getCockpitRangeMapperName(paramClass))

    internal fun createStepMapperFieldSpec(paramClass: Class<*>): FieldSpec =
            createMapperFieldSpec(getParametrizedCockpitStepMapperClassName(paramClass), getCockpitStepMapperName(paramClass))

    internal fun createMapperFieldSpec(mapperClassName: TypeName, mapperFieldName: String): FieldSpec =
            FieldSpec.builder(mapperClassName, mapperFieldName,
                    Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new \$T()", mapperClassName)
                    .build()

    internal fun createGetterMethodSpecForStepParam(paramName: String, stepValue: Number): MethodSpec {
        val stepValueClass = mapToJavaObjectTypeClass(stepValue)
        return createGetterMethodSpecForWrappableParam(paramName, stepValue, getParametrizedCockpitStepClassName(stepValueClass), getCockpitStepMapperName(stepValueClass))
    }

    internal fun createGetterMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createGetterMethodSpecForWrappableParam(paramName, rangeValue, getParametrizedCockpitRangeClassName(rangeValueClass), getCockpitRangeMapperName(rangeValueClass))
    }

    internal fun createGetterMethodSpecForColorParam(paramName: String, param: Any) =
            createGetterMethodSpecForWrappableParam(paramName, param, cockpitColorClassName, COCKPIT_COLOR_MAPPER)

    internal fun createGetterMethodSpecForWrappableParam(paramName: String, param: Any, wrappableClass: TypeName, mapperName: String) =
            createGetterMethodSpecForParamAndConfigurator(paramName, param) { builder ->
                builder.addStatement("\$T value = (\$T) \$T.INSTANCE.getParamValue(\"$paramName\")",
                        wrappableClass, wrappableClass, cockpitManagerClassName)
                builder.addStatement("return $mapperName.unwrap(value)")
            }

    internal fun createGetterMethodSpecForParam(paramName: String, param: Any): MethodSpec =
            createGetterMethodSpecForParamAndConfigurator(paramName, param) { builder ->
                builder.addStatement("return (\$T) \$T.INSTANCE.getParamValue(\"$paramName\")",
                        mapToTypeClass(param), cockpitManagerClassName)
            }

    internal fun createSetterMethodSpecForColorParam(paramName: String, color: CockpitColor): MethodSpec =
            createSetterMethodSpecForWrappableParam(paramName, mapToTypeClass(color), color.value, COCKPIT_COLOR_MAPPER)

    internal fun createSetterMethodSpecForRangeParam(paramName: String, rangeValue: Number) =
            createSetterMethodSpecForWrappableComplexParam(paramName, getParametrizedCockpitRangeClassName(mapToJavaObjectTypeClass(rangeValue)), rangeValue, getCockpitRangeMapperName(mapToJavaObjectTypeClass(rangeValue)))

    internal fun createSetterMethodSpecForStepParam(paramName: String, stepValue: Number) =
            createSetterMethodSpecForWrappableComplexParam(paramName, getParametrizedCockpitStepClassName(mapToJavaObjectTypeClass(stepValue)), stepValue, getCockpitStepMapperName(mapToJavaObjectTypeClass(stepValue)))

    internal fun createSetterMethodSpecForReadOnlyParam(paramName: String, readOnlyValue: CockpitReadOnly) =
            createSetterMethodSpecForWrappableParam(paramName, mapToTypeClass(readOnlyValue), readOnlyValue.text, COCKPIT_READ_ONLY_MAPPER)

    internal fun createSetterMethodSpecForWrappableParam(paramName: String, wrappableClass: Class<*>, param: Any, mapperName: String): MethodSpec =
            createSetterMethodSpecForParam(paramName, param, "value", listOf<MethodSpec.Builder.() -> MethodSpec.Builder>({ addStatement("\$T value = $mapperName.wrap($paramName)", wrappableClass) }))

    internal fun createSetterMethodSpecForWrappableComplexParam(paramName: String, wrappableClass: TypeName, param: Any, mapperName: String): MethodSpec =
            createSetterMethodSpecForParam(paramName, param, "value", listOf<MethodSpec.Builder.() -> MethodSpec.Builder>(
                    {
                        addStatement("\$T base = (\$T) \$T.INSTANCE.getParamValue(\"$paramName\")",
                                wrappableClass, wrappableClass, cockpitManagerClassName)
                    },
                    { addStatement("\$T value = $mapperName.wrap(base, $paramName)", wrappableClass) }
            ))

    internal fun createSetterMethodSpecForParam(paramName: String, param: Any, returnValueName: String = paramName, beforeActions: List<(MethodSpec.Builder.() -> MethodSpec.Builder)>? = null): MethodSpec {
        val builder = MethodSpec.methodBuilder("set${paramName.capitalize()}").apply {
            addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            addParameter(ParameterSpec.builder(mapToTypeClass(param), paramName).build())
            beforeActions?.forEach { it.invoke(this) }
            addStatement("\$T.INSTANCE.setParamValue(\"$paramName\", $returnValueName)", cockpitManagerClassName)
        }

        return builder.build()
    }

    internal fun createDeprecatedAddPropertyChangeListenerMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(
                paramName, rangeValueClass,
                getCockpitRangeMapperName(rangeValueClass),
                getRangeListenerMapName(rangeValueClass),
                getParametrizedCockpitRangeClassName(rangeValueClass),
                deprecated = true
        )
    }

    internal fun createAddForeverPropertyChangeListenerMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(
                paramName, rangeValueClass,
                getCockpitRangeMapperName(rangeValueClass),
                getRangeListenerMapName(rangeValueClass),
                getParametrizedCockpitRangeClassName(rangeValueClass)
        )
    }

    internal fun createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(
                paramName, rangeValueClass,
                getCockpitRangeMapperName(rangeValueClass),
                getRangeListenerMapName(rangeValueClass),
                getParametrizedCockpitRangeClassName(rangeValueClass),
                takeLifecycleOwner = true
        )
    }

    internal fun createDeprecatedAddPropertyChangeListenerMethodSpecForStepParam(paramName: String, stepValue: Number): MethodSpec {
        val stepValueClass = mapToJavaObjectTypeClass(stepValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(
                paramName, stepValueClass,
                getCockpitStepMapperName(stepValueClass),
                getStepListenerMapName(stepValueClass),
                getParametrizedCockpitStepClassName(stepValueClass),
                deprecated = true
        )
    }

    internal fun createAddForeverPropertyChangeListenerMethodSpecForStepParam(paramName: String, stepValue: Number): MethodSpec {
        val stepValueClass = mapToJavaObjectTypeClass(stepValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(
                paramName, stepValueClass,
                getCockpitStepMapperName(stepValueClass),
                getStepListenerMapName(stepValueClass),
                getParametrizedCockpitStepClassName(stepValueClass)
        )
    }

    internal fun createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForStepParam(paramName: String, stepValue: Number): MethodSpec {
        val stepValueClass = mapToJavaObjectTypeClass(stepValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(
                paramName, stepValueClass,
                getCockpitStepMapperName(stepValueClass),
                getStepListenerMapName(stepValueClass),
                getParametrizedCockpitStepClassName(stepValueClass),
                takeLifecycleOwner = true
        )
    }

    internal fun createRemovePropertyChangeListenerMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createRemovePropertyChangeListenerMethodSpecForWrappableParam(paramName, rangeValueClass, getRangeListenerMapName(rangeValueClass), getParametrizedCockpitRangeClassName(rangeValueClass))
    }

    internal fun createRemovePropertyChangeListenerMethodSpecForStepParam(paramName: String, stepValue: Number): MethodSpec {
        val stepValueClass = mapToJavaObjectTypeClass(stepValue)
        return createRemovePropertyChangeListenerMethodSpecForWrappableParam(paramName, stepValueClass, getStepListenerMapName(stepValueClass), getParametrizedCockpitStepClassName(stepValueClass))
    }

    internal fun createDeprecatedAddPropertyChangeListenerMethodSpecForColorParam(paramName: String, color: CockpitColor) =
            createAddPropertyChangeListenerMethodSpecForWrappableParam(
                    paramName, mapToTypeClass(color.value),
                    COCKPIT_COLOR_MAPPER, COLOR_LISTENER_MAP,
                    cockpitColorClassName, deprecated = true
            )

    internal fun createAddForeverPropertyChangeListenerMethodSpecForColorParam(paramName: String, color: CockpitColor) =
            createAddPropertyChangeListenerMethodSpecForWrappableParam(
                    paramName, mapToTypeClass(color.value),
                    COCKPIT_COLOR_MAPPER, COLOR_LISTENER_MAP,
                    cockpitColorClassName
            )

    internal fun createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForColorParam(paramName: String, color: CockpitColor) =
            createAddPropertyChangeListenerMethodSpecForWrappableParam(
                    paramName, mapToTypeClass(color.value),
                    COCKPIT_COLOR_MAPPER, COLOR_LISTENER_MAP,
                    cockpitColorClassName, takeLifecycleOwner = true
            )

    internal fun createRemovePropertyChangeListenerMethodSpecForColorParam(paramName: String, color: CockpitColor) =
            createRemovePropertyChangeListenerMethodSpecForWrappableParam(paramName, mapToTypeClass(color.value), COLOR_LISTENER_MAP, cockpitColorClassName)

    private fun createAddPropertyChangeListenerMethodSpecForWrappableParam(
            paramName: String,
            paramClass: Class<*>,
            mapperName: String,
            listenerMapName: String,
            wrappableClassName: TypeName,
            deprecated: Boolean = false,
            takeLifecycleOwner: Boolean = false): MethodSpec {
        val listenerName = "${paramName}Listener"
        val actions = listOf<MethodSpec.Builder.() -> MethodSpec.Builder>(
                {
                    addStatement("\$T $listenerName = new \$T($LISTENER_ARGUMENT_NAME, $mapperName)",
                            getParametrizedCockpitPropertyChangeListenerClassName(wrappableClassName),
                            ParameterizedTypeName.get(mappingPropertyChangeListenerClassName, wrappableClassName, TypeName.get(paramClass)))
                },
                { addStatement("$listenerMapName.$PUT($LISTENER_ARGUMENT_NAME, $listenerName)") })
        val methodPrefix = if (!deprecated && !takeLifecycleOwner) ADD_FOREVER else ADD
        return createPropertyChangeListenerMethodSpecForParam(paramName, paramClass, methodPrefix, listenerName, actions, deprecated, takeLifecycleOwner)
    }


    private fun createRemovePropertyChangeListenerMethodSpecForWrappableParam(paramName: String, paramClass: Class<*>, listenerMapName: String, wrappableClassName: TypeName): MethodSpec {
        val listenerName = "${paramName}Listener"
        val actions = listOf<MethodSpec.Builder.() -> MethodSpec.Builder>(
                {
                    addStatement("\$T $listenerName = $listenerMapName.get($LISTENER_ARGUMENT_NAME)",
                            getParametrizedCockpitPropertyChangeListenerClassName(wrappableClassName))
                },
                { addStatement("$listenerMapName.$REMOVE($LISTENER_ARGUMENT_NAME)") })
        return createPropertyChangeListenerMethodSpecForParam(paramName, paramClass, REMOVE, listenerName, actions)
    }

    internal fun createDeprecatedAddPropertyChangeListenerMethodSpecForParam(paramName: String, param: Any) =
            createPropertyChangeListenerMethodSpecForParam(paramName, mapToJavaObjectTypeClass(param), ADD, deprecated = true)

    internal fun createLifecycleAcceptingAddPropertyChangeListenerMethodSpecForParam(paramName: String, param: Any) =
            createPropertyChangeListenerMethodSpecForParam(paramName, mapToJavaObjectTypeClass(param), ADD, takeLifecycleOwner = true)

    internal fun createAddForeverPropertyChangeListenerMethodSpecForParam(paramName: String, param: Any) =
            createPropertyChangeListenerMethodSpecForParam(paramName, mapToJavaObjectTypeClass(param), ADD_FOREVER)

    internal fun createRemovePropertyChangeListenerMethodSpecForParam(paramName: String, param: Any) =
            createPropertyChangeListenerMethodSpecForParam(paramName, mapToJavaObjectTypeClass(param), REMOVE)

    private fun createPropertyChangeListenerMethodSpecForParam(paramName: String,
                                                               paramClass: Class<*>,
                                                               actionName: String,
                                                               returnValueName: String = LISTENER_ARGUMENT_NAME,
                                                               beforeActions: List<(MethodSpec.Builder.() -> MethodSpec.Builder)>? = null,
                                                               deprecated: Boolean = false,
                                                               takeLifecycleOwner: Boolean = false): MethodSpec {
        val builder = MethodSpec.methodBuilder("${actionName}On${paramName.capitalize()}ChangeListener").apply {
            if (deprecated)
                addAnnotation(
                        AnnotationSpec
                                .builder(Deprecated::class.java)
                                .addMember("message",
                                        "\"This method might leak context. Consider " +
                                                "using overload taking lifecycleOwner or switch to addForever(...)\"")
                                .build()
                )
            addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            if (takeLifecycleOwner)
                addParameter(ParameterSpec.builder(androidLifecycleOwnerClassName, LIFECYCLE_OWNER).build())
            addParameter(ParameterSpec.builder(getParametrizedCockpitPropertyChangeListenerClassName(paramClass), LISTENER_ARGUMENT_NAME).build())
            beforeActions?.forEach { it.invoke(this) }

            if (takeLifecycleOwner)
                addStatement("\$T.INSTANCE.${actionName}OnParamChangeListener($LIFECYCLE_OWNER, \"$paramName\", $returnValueName)", cockpitManagerClassName)
            else
                addStatement("\$T.INSTANCE.${actionName}OnParamChangeListener(\"$paramName\", $returnValueName)", cockpitManagerClassName)
        }
        return builder
                .build()
    }

    internal fun createDeprecatedAddActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>) =
            createActionRequestCallbackMethodSpecForParam(param, ADD, deprecated = true)

    internal fun createLifecycleAcceptingAddActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>) =
            createActionRequestCallbackMethodSpecForParam(param, ADD, takeLifecycleOwner = true)

    internal fun createAddForeverActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>) =
            createActionRequestCallbackMethodSpecForParam(param, ADD_FOREVER)

    internal fun createRemoveActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>) =
            createActionRequestCallbackMethodSpecForParam(param, REMOVE)

    private fun createActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>,
                                                              methodPrefix: String,
                                                              deprecated: Boolean = false,
                                                              takeLifecycleOwner: Boolean = false): MethodSpec {
        val callbackParamName = "callback"
        val actionRequestCallbackName = "ActionRequestCallback"
        return MethodSpec.methodBuilder("$methodPrefix${param.name.capitalize()}$actionRequestCallbackName")
                .apply {
                    if (deprecated && methodPrefix == ADD)
                        addAnnotation(
                                AnnotationSpec
                                        .builder(Deprecated::class.java)
                                        .addMember("message",
                                                "\"This method might leak context. Consider " +
                                                        "using overload taking lifecycleOwner or switch to addForever(...)\"")
                                        .build()
                        )
                }
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .apply {
                    if (takeLifecycleOwner)
                        addParameter(ParameterSpec.builder(androidLifecycleOwnerClassName, LIFECYCLE_OWNER).build())
                }
                .addParameter(ParameterSpec.builder(actionRequestCallbackClassName, callbackParamName).build())
                .apply {
                    if (takeLifecycleOwner)
                        addStatement("\$T.INSTANCE.$methodPrefix$actionRequestCallbackName($LIFECYCLE_OWNER, \"${param.name}\", $callbackParamName)",
                                cockpitManagerClassName)
                    else
                        addStatement("\$T.INSTANCE.$methodPrefix$actionRequestCallbackName(\"${param.name}\", $callbackParamName)",
                                cockpitManagerClassName)
                }
                .build()
    }

    internal fun createSelectedValueGetterMethodSpecForParam(param: CockpitParam<CockpitListType<*>>): MethodSpec {
        val returnType = mapToTypeClass(param.value.items[0])

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

    companion object {
        private const val ADD = "add"
        private const val ADD_FOREVER = "addForever"
        private const val PUT = "put"
        private const val REMOVE = "remove"

        private const val COCKPIT_COLOR_MAPPER = "cockpitColorMapper"
        private const val COCKPIT_READ_ONLY_MAPPER = "cockpitReadOnlyMapper"
        private fun getCockpitRangeMapperName(paramClass: Class<*>) = "cockpitRange${paramClass.simpleName}Mapper"
        private fun getCockpitStepMapperName(paramClass: Class<*>) = "cockpitStep${paramClass.simpleName}Mapper"

        private const val COLOR_LISTENER_MAP = "colorListenerMap"
        private fun getRangeListenerMapName(paramClass: Class<*>) = "range${paramClass.simpleName}ListenerMap"
        private fun getStepListenerMapName(paramClass: Class<*>) = "step${paramClass.simpleName}ListenerMap"

        private const val LISTENER_ARGUMENT_NAME = "listener"
        private const val LIFECYCLE_OWNER = "lifecycleOwner"
    }
}
