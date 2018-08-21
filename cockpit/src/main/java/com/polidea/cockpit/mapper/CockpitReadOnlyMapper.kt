package com.polidea.cockpit.mapper

import com.polidea.cockpit.core.type.CockpitReadOnly

class CockpitReadOnlyMapper : ValueMapper<CockpitReadOnly, String> {

    override fun wrap(value: String) = CockpitReadOnly(value)

    override fun unwrap(wrapper: CockpitReadOnly) = wrapper.text
}