package com.polidea.cockpit.mapper

interface ComplexValueWrapper<W : Any, V : Any> {
    fun wrap(base: W, value: V): W
}