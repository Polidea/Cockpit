package com.polidea.cockpit.paramsedition.viewholder

import android.view.View
import android.widget.ImageButton
import com.polidea.cockpit.R

abstract class ParamBaseValueViewHolder<T : Any>(view: View) : ParamBaseViewHolder<T>(view) {

    var valueChangeListener: ((T) -> Unit)? = null
}