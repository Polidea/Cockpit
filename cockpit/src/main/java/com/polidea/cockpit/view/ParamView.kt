package com.polidea.cockpit.view

import android.view.View


interface ParamView<T : Any> {
    val paramName: String
    var value: T

    fun getValueView(): View

    fun getCurrentValue(): T
}