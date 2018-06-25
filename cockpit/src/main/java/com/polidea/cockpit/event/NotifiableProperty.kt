package com.polidea.cockpit.event

open class NotifiableProperty<T> {

    private val listeners = HashSet<PropertyChangeListener<T>>()

    fun addPropertyChangeListener(listener: PropertyChangeListener<T>) {
        listeners.add(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener<T>) {
        listeners.remove(listener)
    }

    protected fun firePropertyChange(oldValue: T, newValue: T) {
        listeners.forEach { it.onValueChange(oldValue, newValue) }
    }
}