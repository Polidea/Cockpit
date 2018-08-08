package com.polidea.cockpit.mapper

import com.polidea.cockpit.core.type.CockpitRange

class CockpitRangeMapper<T : Number> : ComplexValueMapper<CockpitRange<T>, T> {

    override fun unwrap(wrapper: CockpitRange<T>) = wrapper.value

    override fun wrap(base: CockpitRange<T>, value: T) = base.copy(value = value)
}