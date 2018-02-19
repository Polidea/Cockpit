package com.polidea.androidtweaks.view

import android.view.View


interface ParamView<T> {
    val paramName: String
    var value: T

    fun getValueView(): View

    fun getCurrentValue(): T
}