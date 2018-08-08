package com.polidea.cockpit.mapper

interface ValueMapper<W : Any, V : Any> : ValueWrapper<W, V>, ValueUnwrapper<W, V>