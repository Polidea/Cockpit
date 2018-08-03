package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitColor
import com.polidea.cockpit.core.type.CockpitListType
import com.polidea.cockpit.core.type.CockpitRange
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
                    is CockpitColor -> {
                        add(createGetterMethodSpecForColorParam(paramName, paramValue))
                        add(createSetterMethodSpecForColorParam(paramName, paramValue))
                        add(createAddPropertyChangeListenerMethodSpecForColorParam(paramName, paramValue))
                        add(createRemovePropertyChangeListenerMethodSpecForColorParam(paramName, paramValue))
                    }
                    is CockpitRange<*> -> {
                        add(createGetterMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createSetterMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createAddPropertyChangeListenerMethodSpecForRangeParam(paramName, paramValue.value))
                        add(createRemovePropertyChangeListenerMethodSpecForRangeParam(paramName, paramValue.value))
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

    private fun createListenerMapFieldSpec(mapName: String, wrappablePropertyChangeListener: TypeName, paramPropertyChangeListener: TypeName): FieldSpec {
        return FieldSpec.builder(ParameterizedTypeName.get(mapClassName, paramPropertyChangeListener, wrappablePropertyChangeListener),
                mapName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new \$T<>()", hashMapClassName)
                .build()
    }

    internal fun createColorMapperFieldSpec(): FieldSpec =
            createMapperFieldSpec(cockpitColorMapperClassName, COCKPIT_COLOR_MAPPER)

    internal fun createRangeMapperFieldSpec(paramClass: Class<*>): FieldSpec =
            createMapperFieldSpec(getParametrizedCockpitRangeMapperClassName(paramClass), getCockpitRangeMapperName(paramClass))

    internal fun createMapperFieldSpec(mapperClassName: TypeName, mapperFieldName: String): FieldSpec =
            FieldSpec.builder(mapperClassName, mapperFieldName,
                    Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new \$T()", mapperClassName)
                    .build()

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

    internal fun createAddPropertyChangeListenerMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createAddPropertyChangeListenerMethodSpecForWrappableParam(paramName, rangeValueClass, getCockpitRangeMapperName(rangeValueClass), getRangeListenerMapName(rangeValueClass), getParametrizedCockpitRangeClassName(rangeValueClass))
    }

    internal fun createRemovePropertyChangeListenerMethodSpecForRangeParam(paramName: String, rangeValue: Number): MethodSpec {
        val rangeValueClass = mapToJavaObjectTypeClass(rangeValue)
        return createRemovePropertyChangeListenerMethodSpecForWrappableParam(paramName, rangeValueClass, getRangeListenerMapName(rangeValueClass), getParametrizedCockpitRangeClassName(rangeValueClass))
    }

    internal fun createAddPropertyChangeListenerMethodSpecForColorParam(paramName: String, color: CockpitColor) =
            createAddPropertyChangeListenerMethodSpecForWrappableParam(paramName, mapToTypeClass(color.value), COCKPIT_COLOR_MAPPER, COLOR_LISTENER_MAP, cockpitColorClassName)

    internal fun createRemovePropertyChangeListenerMethodSpecForColorParam(paramName: String, color: CockpitColor) =
            createRemovePropertyChangeListenerMethodSpecForWrappableParam(paramName, mapToTypeClass(color.value), COLOR_LISTENER_MAP, cockpitColorClassName)

    private fun createAddPropertyChangeListenerMethodSpecForWrappableParam(paramName: String, paramClass: Class<*>, mapperName: String, listenerMapName: String, wrappableClassName: TypeName): MethodSpec {
        val listenerName = "${paramName}Listener"
        val actions = listOf<MethodSpec.Builder.() -> MethodSpec.Builder>(
                {
                    addStatement("\$T $listenerName = new \$T($LISTENER_ARGUMENT_NAME, $mapperName)",
                            getParametrizedCockpitPropertyChangeListenerClassName(wrappableClassName),
                            ParameterizedTypeName.get(mappingPropertyChangeListenerClassName, wrappableClassName, TypeName.get(paramClass)))
                },
                { addStatement("$listenerMapName.$PUT($LISTENER_ARGUMENT_NAME, $listenerName)") })
        return createPropertyChangeListenerMethodSpecForParam(paramName, paramClass, ADD, listenerName, actions)
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

    internal fun createAddPropertyChangeListenerMethodSpecForParam(paramName: String, param: Any) =
            createPropertyChangeListenerMethodSpecForParam(paramName, mapToJavaObjectTypeClass(param), ADD)

    internal fun createRemovePropertyChangeListenerMethodSpecForParam(paramName: String, param: Any) =
            createPropertyChangeListenerMethodSpecForParam(paramName, mapToJavaObjectTypeClass(param), REMOVE)

    private fun createPropertyChangeListenerMethodSpecForParam(paramName: String, paramClass: Class<*>, actionName: String, returnValueName: String = LISTENER_ARGUMENT_NAME, beforeActions: List<(MethodSpec.Builder.() -> MethodSpec.Builder)>? = null): MethodSpec {
        val builder = MethodSpec.methodBuilder("${actionName}On${paramName.capitalize()}ChangeListener").apply {
            addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            addParameter(ParameterSpec.builder(getParametrizedCockpitPropertyChangeListenerClassName(paramClass), LISTENER_ARGUMENT_NAME).build())
            beforeActions?.forEach { it.invoke(this) }
            addStatement("\$T.INSTANCE.${actionName}OnParamChangeListener(\"$paramName\", $returnValueName)", cockpitManagerClassName)
        }
        return builder
                .build()
    }

    internal fun createAddActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>) =
            createActionRequestCallbackMethodSpecForParam(param, ADD)

    internal fun createRemoveActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>) =
            createActionRequestCallbackMethodSpecForParam(param, REMOVE)

    private fun createActionRequestCallbackMethodSpecForParam(param: CockpitParam<*>, methodPrefix: String): MethodSpec {
        val callbackParamName = "callback"
        val actionRequestCallbackName = "ActionRequestCallback"
        return MethodSpec.methodBuilder("$methodPrefix${param.name.capitalize()}$actionRequestCallbackName")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(actionRequestCallbackClassName, callbackParamName).build())
                .addStatement("\$T.INSTANCE.$methodPrefix$actionRequestCallbackName(\"${param.name}\", $callbackParamName)",
                        cockpitManagerClassName)
                .build()
    }

    internal fun createAddSelectionChangeListenerMethodSpecForParam(param: CockpitParam<CockpitListType<*>>) =
            createSelectionChangeListenerMethodSpecForParam(param, ADD)

    internal fun createRemoveSelectionChangeListenerMethodSpecForParam(param: CockpitParam<CockpitListType<*>>) =
            createSelectionChangeListenerMethodSpecForParam(param, REMOVE)

    private fun createSelectionChangeListenerMethodSpecForParam(param: CockpitParam<CockpitListType<*>>, methodPrefix: String): MethodSpec {
        val selectionChangeListenerName = "SelectionChangeListener"
        return MethodSpec.methodBuilder("$methodPrefix${param.name.capitalize()}$selectionChangeListenerName")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(getParametrizedCockpitSelectionChangeListenerClassName(mapToTypeClass(param.value.getSelectedItem())), LISTENER_ARGUMENT_NAME).build())
                .addStatement("\$T.INSTANCE.$methodPrefix$selectionChangeListenerName(\"${param.name}\", $LISTENER_ARGUMENT_NAME)",
                        cockpitManagerClassName)
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
        private const val PUT = "put"
        private const val REMOVE = "remove"

        private const val COCKPIT_COLOR_MAPPER = "cockpitColorMapper"
        private fun getCockpitRangeMapperName(paramClass: Class<*>) = "cockpitRange${paramClass.simpleName}Mapper"

        private const val COLOR_LISTENER_MAP = "colorListenerMap"
        private fun getRangeListenerMapName(paramClass: Class<*>) = "range${paramClass.simpleName}ListenerMap"

        private const val LISTENER_ARGUMENT_NAME = "listener"
    }
}
