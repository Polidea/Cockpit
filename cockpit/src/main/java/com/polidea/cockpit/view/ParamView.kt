package com.polidea.cockpit.view

import android.view.View


interface ParamView<T : Any> {
    val paramName: String
    var value: T
    val description: String?
    val group: String?

    fun getValueView(): View
    fun getRestoreButton(): View
}