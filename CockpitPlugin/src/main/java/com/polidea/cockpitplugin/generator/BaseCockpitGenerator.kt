package com.polidea.cockpitplugin.generator

import com.polidea.cockpitplugin.core.Param
import com.squareup.javapoet.*
import java.io.File
import javax.lang.model.element.Modifier

abstract class BaseCockpitGenerator {

    abstract fun generate(params: List<Param<*>>, file: File?)

    protected val cockpitPackage = "com.polidea.cockpit.cockpit"
    protected val cockpitManagerPackage = "com.polidea.cockpit.manager"
    protected val androidContentPackage = "android.content"
    protected val cockpitActivityPackage = "com.polidea.cockpit.activity"
    protected val cockpitUtilsPackage = "com.polidea.cockpit.utils"
    protected val cockpitEventPackage = "com.polidea.cockpit.event"

    protected val cockpit = "Cockpit"
    protected val cockpitManager = "CockpitManager"
    protected val cockpitParam = "CockpitParam"
    protected val intent = "Intent"
    protected val context = "Context"
    protected val cockpitActivity = "CockpitActivity"
    protected val fileUtils = "FileUtils"
    protected val propertyChangeListener = "PropertyChangeListener"

    protected val cockpitManagerClassName = ClassName.get(cockpitManagerPackage, cockpitManager)
    protected val cockpitParamClassName = ClassName.get(cockpitManagerPackage, cockpitParam)
    protected val androidIntentClassName = ClassName.get(androidContentPackage, intent)
    protected val androidContextClassName = ClassName.get(androidContentPackage, context)
    protected val cockpitActivityClassName = ClassName.get(cockpitActivityPackage, cockpitActivity)
    protected val fileUtilsClassName = ClassName.get(cockpitUtilsPackage, fileUtils)
    protected val propertyChangeListenerClassName = ClassName.get(cockpitEventPackage, propertyChangeListener)

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

    inline protected fun <T : Any> createGetterMethodSpecForParamAndConfigurator(param: Param<T>,
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

    protected fun mapToTypeClass(param: Param<*>) = param.value::class.java

    protected fun mapToJavaObjectTypeClass(param: Param<*>) = param.value::class.javaObjectType

    protected fun createWrappedValueForParam(param: Param<*>): Any {
        return when (param.value.javaClass) {
            String::class.java -> "\"${param.value}\""
            else -> param.value
        }
    }
}