package com.polidea.cockpit.mapper

interface ComplexValueMapper<W : Any, V : Any> : ComplexValueWrapper<W, V>, ValueUnwrapper<W, V>