package com.polidea.cockpit.mapper

interface ValueWrapper<W : Any, V : Any> {
    fun wrap(value: V): W
}