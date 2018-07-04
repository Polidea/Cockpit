package com.polidea.cockpit.manager

import com.polidea.cockpit.event.PropertyChangeListener

class ParamChangeNotifier {

    private val listeners: MutableMap<String, MutableSet<*>> = mutableMapOf()

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

    private inline fun <reified T : Any> getListeners(name: String): T =
            (listeners[name] ?: mutableSetOf<T>()).let { it as T }
}