package com.polidea.cockpitplugin.generator

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.type.core.CockpitListType
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier

internal abstract class BaseCockpitGenerator {

    abstract fun generate(params: List<CockpitParam<*>>, file: File?)

    private val cockpitPackage = "com.polidea.cockpit.cockpit"
    private val cockpitManagerPackage = "com.polidea.cockpit.manager"
    private val androidSupportV4Package = "android.support.v4.app"
    private val cockpitDialogPackage = "com.polidea.cockpit.paramsedition"
    private val cockpitEventPackage = "com.polidea.cockpit.event"

    protected val javaUtilPackage = "java.util"

    private val cockpit = "Cockpit"
    private val cockpitManager = "CockpitManager"
    private val fragmentManager = "FragmentManager"
    private val cockpitDialog = "CockpitDialog"
    private val propertyChangeListener = "PropertyChangeListener"
    private val actionRequestCallback = "ActionRequestCallback"
    private val selectionChangeListener = "SelectionChangeListener"

    protected val arrays = "Arrays"

    protected val cockpitManagerClassName = ClassName.get(cockpitManagerPackage, cockpitManager)
    protected val androidFragmentManagerClassName = ClassName.get(androidSupportV4Package, fragmentManager)
    protected val cockpitDialogClassName = ClassName.get(cockpitDialogPackage, cockpitDialog)
    protected val propertyChangeListenerClassName = ClassName.get(cockpitEventPackage, propertyChangeListener)
    protected val actionRequestCallbackClassName = ClassName.get(cockpitEventPackage, actionRequestCallback)
    protected val selectionChangeListenerClassName = ClassName.get(cockpitEventPackage, selectionChangeListener)

    protected val arraysClassName = ClassName.get(javaUtilPackage, arrays)

    protected fun getParametrizedCockpitPropertyChangeListenerClassName(clazz: Class<*>) =
            ParameterizedTypeName.get(propertyChangeListenerClassName, WildcardTypeName.subtypeOf(clazz))

    protected fun generate(file: File?, configurator: (TypeSpec.Builder) -> TypeSpec.Builder) {

        val cockpitClass = configurator(TypeSpec.classBuilder(cockpit)
                .addModifiers(Modifier.PUBLIC))
                .build()

        val cockpitFile = JavaFile.builder(cockpitPackage, cockpitClass).build()

        if (file == null) {
            cockpitFile.writeTo(System.out)
        } else {
            cockpitFile.writeTo(file)
        }
    }

    protected fun <T : Any> createGetterMethodSpecForParamAndConfigurator(param: CockpitParam<T>,
                                                                          configurator: (MethodSpec.Builder) -> MethodSpec.Builder): MethodSpec {
        val prefix = when (param.value) {
            is Boolean -> "is"
            else -> "get"
        }
        return configurator(MethodSpec.methodBuilder("$prefix${param.name.capitalize()}")
                .returns(mapToTypeClass(param))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC))
                .build()
    }

    protected fun mapToTypeClass(param: CockpitParam<*>): Class<*> {
        return when (param.value) {
            is Boolean -> Boolean::class.java
            is Double -> Double::class.java
            is Int -> Int::class.java
            else -> param.value::class.java
        }
    }

    protected fun mapToJavaObjectTypeClass(param: CockpitParam<*>) = param.value::class.javaObjectType
}