package com.polidea.cockpit.event

interface PropertyChangeListener<T> {
    fun onValueChange(oldValue: T, newValue: T)
}