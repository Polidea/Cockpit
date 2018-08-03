package com.polidea.cockpit.mapper

interface ValueUnwrapper<W : Any, V : Any> {
    fun unwrap(wrapper: W): V
}