package com.polidea.cockpit.manager

import com.polidea.cockpit.listener.CockpitParamChangeListener
import kotlin.properties.Delegates

data class CockpitParam<T>(val name: String, val typeClass: Class<*>, private var _value: T) {

    var cockpitParamChangeListener: CockpitParamChangeListener<T>? = null

    var value: T by Delegates.observable(_value) { _, old, new ->
        _value = new
        if (old != null && new != null && old != new)
            cockpitParamChangeListener?.onValueChange(old, new)
    }
}