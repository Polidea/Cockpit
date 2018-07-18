package com.polidea.cockpit.manager

import com.polidea.cockpit.core.CockpitParam
import com.polidea.cockpit.core.type.CockpitAction
import com.polidea.cockpit.core.type.CockpitListType
import com.polidea.cockpit.event.ActionRequestCallback
import com.polidea.cockpit.event.PropertyChangeListener
import com.polidea.cockpit.event.SelectionChangeListener
import com.polidea.cockpit.utils.FileUtils
import com.polidea.cockpit.utils.copy
import com.polidea.cockpit.utils.getParam
import org.jetbrains.annotations.TestOnly

object CockpitManager {

    private val params: MutableList<CockpitParam<Any>> by lazy {
        FileUtils.getParams().toMutableList()
    }

    private val defaultParams: List<CockpitParam<Any>> by lazy {
        FileUtils.getDefaultParams()
    }

    private val paramChangeNotifier = ParamChangeNotifier()

    private val callbackNotifier = CallbackNotifier()

    internal fun addParam(param: CockpitParam<*>) {
        checkIfExistsAndAddParam(param)
    }

    private fun checkIfExistsAndAddParam(param: CockpitParam<*>) {
        if (!exists(param.name)) {
            System.out.println("Param ${param.name} doesn't exist, adding")
            params.add(CockpitParam(param.name, param.value, param.description, param.group))
        } else {
            System.out.println("Param ${param.name} already exists")
        }
    }

    private fun exists(key: String) =
            params.find { it.name == key } != null

    fun <T : Any> getParamValue(name: String): T = params.getParam<CockpitParam<T>>(name).value

    fun <T : Any> getParamDefaultValue(name: String): T = defaultParams.getParam<CockpitParam<T>>(name).value

    internal fun setParamValues(params: Collection<CockpitParam<Any>>) {
        params.forEach { setParamValue(it.name, it.value) }
    }

    fun <T : Any> setParamValue(name: String, newValue: T) {
        val param = params.getParam<CockpitParam<T>>(name)
        val oldValue = param.value
        param.value = newValue
        paramChangeNotifier.firePropertyChange(name, oldValue, newValue)
    }

    fun <T : Any> addOnParamChangeListener(name: String, listener: PropertyChangeListener<T>) {
        paramChangeNotifier.add(name, listener)
    }

    fun <T : Any> removeOnParamChangeListener(name: String, listener: PropertyChangeListener<T>) {
        paramChangeNotifier.remove(name, listener)
    }

    fun <T: Any> selectParamValue(name: String, selectedIndex: Int) {
        val param = params.getParam<CockpitParam<CockpitListType<T>>>(name)
        val previouslySelectedValue = param.value.getSelectedItem()
        param.value.selectedIndex = selectedIndex
        val currentlySelectedValue = param.value.getSelectedItem()
        paramChangeNotifier.fireValueSelection(name, previouslySelectedValue, currentlySelectedValue)
    }

    fun <T : Any> getSelectedValue(name: String): T {
        val param = params.getParam<CockpitParam<CockpitListType<T>>>(name)
        return param.value.getSelectedItem()
    }

    fun <T : Any> addSelectionChangeListener(name: String, listener: SelectionChangeListener<T>) {
        paramChangeNotifier.add(name, listener)
    }

    fun <T : Any> removeSelectionChangeListener(name: String, listener: SelectionChangeListener<T>) {
        paramChangeNotifier.remove(name, listener)
    }

    fun addActionRequestCallback(name: String, callback: ActionRequestCallback) {
        callbackNotifier.add(name, callback)
    }

    fun removeActionRequestCallback(name: String, callback: ActionRequestCallback) {
        callbackNotifier.remove(name, callback)
    }

    fun requestAction(name: String) {
        callbackNotifier.requestAction(name)
    }

    internal fun getParamsCopy(): List<CockpitParam<Any>> = params.copy()

    internal fun getDefaultParamsCopy(): List<CockpitParam<Any>> = defaultParams.copy()

    fun save() {
        // CockpitAction param should not be persisted because there is no getter and setter that
        // can change its value. `buttonText` property should be always loaded from cockpit.yml
        FileUtils.saveCockpitAsYaml(params.filter { it.value !is CockpitAction })
    }

    @TestOnly
    internal fun clear() {
        params.clear()
    }
}
