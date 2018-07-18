package com.polidea.cockpit.manager

import com.polidea.cockpit.event.PropertyChangeListener
import com.polidea.cockpit.event.SelectionChangeListener

internal class ParamChangeNotifier {

    private val listeners: MutableMap<String, MutableSet<*>> = mutableMapOf()
    private val selectionListeners: MutableMap<String, MutableSet<SelectionChangeListener>> = mutableMapOf()

    fun <T : Any> add(paramName: String, listener: PropertyChangeListener<T>) {
        val paramListeners = getListeners<MutableSet<PropertyChangeListener<T>>>(paramName)
        paramListeners.add(listener)
        listeners[paramName] = paramListeners
    }

    fun <T : Any> remove(paramName: String, listener: PropertyChangeListener<T>) {
        listeners[paramName]?.remove(listener)
    }

    fun <T : Any> firePropertyChange(paramName: String, oldValue: T, newValue: T) {
        if (oldValue == newValue)
            return

        val paramListeners = getListeners<MutableSet<PropertyChangeListener<T>>>(paramName)
        paramListeners.forEach {
            it.onValueChange(oldValue, newValue)
        }
    }

    fun add(paramName: String, listener: SelectionChangeListener) {
        val paramListeners = getSelectionListeners(paramName)
        paramListeners.add(listener)
        selectionListeners[paramName] = paramListeners
    }

    fun remove(paramName: String, listener: SelectionChangeListener) {
        selectionListeners[paramName]?.remove(listener)
    }

    fun fireValueSelection(paramName: String, selectionIndex: Int) {
        val paramListeners = getSelectionListeners(paramName)
        paramListeners.forEach {
            it.onValueSelected(selectionIndex)
        }
    }

    private inline fun <reified T : Any> getListeners(name: String): T =
            (listeners[name] ?: mutableSetOf<T>()).let { it as T }

    private fun getSelectionListeners(name: String): MutableSet<SelectionChangeListener> = selectionListeners[name] ?: mutableSetOf()
}