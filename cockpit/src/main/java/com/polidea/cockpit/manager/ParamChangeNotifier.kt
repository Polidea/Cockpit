package com.polidea.cockpit.manager

import com.polidea.cockpit.event.PropertyChangeListener
import com.polidea.cockpit.event.SelectionChangeListener

internal class ParamChangeNotifier {

    private val listeners: MutableMap<String, MutableSet<*>> = mutableMapOf()
    private val selectionListeners: MutableMap<String, MutableSet<*>> = mutableMapOf()

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

    fun <T: Any> add(paramName: String, listener: SelectionChangeListener<T>) {
        val paramListeners = getSelectionListeners<T>(paramName)
        paramListeners.add(listener)
        selectionListeners[paramName] = paramListeners
    }

    fun <T: Any> remove(paramName: String, listener: SelectionChangeListener<T>) {
        selectionListeners[paramName]?.remove(listener)
    }

    fun <T: Any >fireValueSelection(paramName: String, previouslySelectedItem: T, selectedItem: T) {
        val paramListeners = getSelectionListeners<T>(paramName)
        paramListeners.forEach {
            if (previouslySelectedItem != selectedItem) {
                it.onValueSelected(selectedItem)
            }
        }
    }

    private inline fun <reified T : Any> getListeners(name: String): T =
            (listeners[name] ?: mutableSetOf<T>()).let { it as T }

    private fun <T: Any> getSelectionListeners(name: String): MutableSet<SelectionChangeListener<T>> = selectionListeners[name] as MutableSet<SelectionChangeListener<T>>? ?: mutableSetOf()
}