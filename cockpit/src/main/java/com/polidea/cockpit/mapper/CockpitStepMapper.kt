package com.polidea.cockpit.mapper

import com.polidea.cockpit.core.type.CockpitStep

class CockpitStepMapper<T : Number> : ComplexValueMapper<CockpitStep<T>, T> {

    override fun unwrap(wrapper: CockpitStep<T>) = wrapper.value

    override fun wrap(base: CockpitStep<T>, value: T) = base.copy(value = value)
}