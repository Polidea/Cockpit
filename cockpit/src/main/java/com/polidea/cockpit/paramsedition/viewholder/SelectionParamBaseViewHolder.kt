package com.polidea.cockpit.paramsedition.viewholder

import android.view.View


internal abstract class SelectionParamBaseViewHolder<T : Any>(view: View) : ParamBaseViewHolder<T>(view) {
    var valueSelectedListener: ((Int) -> Unit)? = null
}