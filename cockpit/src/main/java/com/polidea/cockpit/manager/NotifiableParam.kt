package com.polidea.cockpit.manager

import com.polidea.cockpit.core.Param
import com.polidea.cockpit.event.NotifiableProperty
import kotlin.properties.Delegates

data class NotifiableParam<T : Any>(override val name: String, private var _value: T, override val description: String?, override val group: String?) : NotifiableProperty<T>(), Param<T> {

    override var value: T by Delegates.observable(_value) { _, old, new ->
        _value = new
        if (old != new)
            firePropertyChange(old, new)
    }
}