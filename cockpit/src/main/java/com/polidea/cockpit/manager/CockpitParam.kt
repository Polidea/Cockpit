package com.polidea.cockpit.manager

import com.polidea.cockpit.event.NotifiableProperty
import kotlin.properties.Delegates

data class CockpitParam<T>(val name: String, private var _value: T) : NotifiableProperty<T>() {

    var value: T by Delegates.observable(_value) { _, old, new ->
        _value = new
        if (old != new)
            firePropertyChange(old, new)
    }
}