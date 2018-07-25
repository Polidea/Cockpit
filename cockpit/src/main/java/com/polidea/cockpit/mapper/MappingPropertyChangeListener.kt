package com.polidea.cockpit.mapper

import com.polidea.cockpit.event.PropertyChangeListener

open class MappingPropertyChangeListener<I: Any, O: Any>(private val listener: PropertyChangeListener<O>, private val mapper: ValueMapper<I, O>): PropertyChangeListener<I> {

    override fun onValueChange(oldValue: I, newValue: I) {
        val oldUnwrappedValue: O = mapper.unwrap(oldValue)
        val newUnwrappedValue: O = mapper.unwrap(newValue)
        listener.onValueChange(oldUnwrappedValue, newUnwrappedValue)
    }
}