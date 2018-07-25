package com.polidea.cockpit.mapper

import com.polidea.cockpit.core.type.CockpitColor

class CockpitColorMapper: ValueMapper<CockpitColor, String> {

    override fun wrap(value: String) = CockpitColor(value)

    override fun unwrap(wrapper: CockpitColor) = wrapper.value
}