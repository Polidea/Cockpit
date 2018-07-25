package com.polidea.cockpit.mapper

interface ValueMapper<W: Any, V: Any> {
    fun unwrap(wrapper: W): V
    fun wrap(value: V): W
}