package com.polidea.cockpit.paramsedition.viewholder

import android.view.View

internal abstract class ParamBaseValueViewHolder<T : Any>(view: View) : ParamBaseViewHolder<T>(view) {

    var valueChangeListener: ((T) -> Unit)? = null
}