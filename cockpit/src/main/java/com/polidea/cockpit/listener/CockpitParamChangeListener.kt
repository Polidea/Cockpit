package com.polidea.cockpit.listener

interface CockpitParamChangeListener<T> {
    fun onValueChange(oldValue: T, newValue: T)
}